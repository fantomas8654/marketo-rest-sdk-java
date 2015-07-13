package com.smartling.marketo.sdk.rest;

import com.google.common.base.Preconditions;
import com.smartling.marketo.sdk.Email;
import com.smartling.marketo.sdk.EmailContentItem;
import com.smartling.marketo.sdk.FolderDetails;
import com.smartling.marketo.sdk.FolderId;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoClient;
import com.smartling.marketo.sdk.rest.command.*;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;

import java.util.Collections;
import java.util.List;

public class MarketoRestClient implements MarketoClient {
    private static final String LIST_END_REACHED_CODE = "702";

    private final HttpCommandExecutor httpCommandExecutor;

    private MarketoRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    public static Builder create(String identityUrl, String restUrl) {
        Preconditions.checkNotNull(identityUrl, "Identity URL is empty");
        Preconditions.checkNotNull(restUrl, "REST endpoint URL is empty");

        return new Builder(identityUrl, restUrl);
    }

    @Override
    public List<Email> listEmails(int offset, int limit) throws MarketoApiException {
        return listEmails(offset, limit, null, null);
    }

    @Override
    public List<Email> listEmails(int offset, int limit, FolderId folder, Email.Status status) throws MarketoApiException {
        try {
            return httpCommandExecutor.execute(new GetEmailsCommand(offset, limit, folder, status));
        } catch (MarketoApiException e) {
            if (LIST_END_REACHED_CODE.equalsIgnoreCase(e.getErrorCode())) {
                return Collections.emptyList();
            } else {
                throw e;
            }
        }
    }

    @Override
    public Email loadEmailById(int id) throws MarketoApiException {
        List<Email> execute = httpCommandExecutor.execute(new LoadEmailById(id));
        return execute.get(0);
    }

    @Override
    public Email loadEmailByName(final String name) throws MarketoApiException {
        List<Email> execute = httpCommandExecutor.execute(new LoadEmailByName(name));

        return execute.get(0);
    }

    @Override
    public List<EmailContentItem> loadEmailContent(int emailId) throws MarketoApiException {
        return httpCommandExecutor.execute(new LoadEmailContent(emailId));
    }

    @Override
    public Email cloneEmail(int sourceEmailId, String newEmailName, int folderId) throws MarketoApiException {
        List<Email> cloned = httpCommandExecutor.execute(new CloneEmail(sourceEmailId, newEmailName, folderId));
        return cloned.get(0);
    }

    @Override
    public Email cloneEmail(Email existingEmail, String newEmailName) throws MarketoApiException {
        return cloneEmail(existingEmail.getId(), newEmailName, existingEmail.getFolder().getValue());
    }

    @Override
    public void updateEmailContent(int id, List<EmailContentItem> contentItems) throws MarketoApiException {
        for (EmailContentItem contentItem : contentItems) {
            httpCommandExecutor.execute(new UpdateEmailEditableSection(id, contentItem));
        }
    }

    @Override
    public void updateEmail(Email email) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateEmailContent(email));
    }

    @Override
    public List<FolderDetails> getFolders(FolderId root, int offset, int maxDepth, int limit, String workspace) throws MarketoApiException {
        List<FolderDetails> folders = httpCommandExecutor.execute(new GetFoldersCommand(root, offset, maxDepth, limit, workspace));

        return folders != null ? folders : Collections.<FolderDetails>emptyList();
    }

    public final static class Builder {
        private final String identityUrl;
        private final String restUrl;
        private int connectionTimeout;
        private int socketReadTimeout;

        private Builder(String identityUrl, String restUrl) {
            this.identityUrl = identityUrl;
            this.restUrl = restUrl;
        }

        public MarketoRestClient withCredentials(String clientId, String clientSecret) {
            Preconditions.checkNotNull(clientId, "Client ID is empty");
            Preconditions.checkNotNull(clientSecret, "Client secret is empty");

            HttpCommandExecutor executor = new HttpCommandExecutor(identityUrl, restUrl, clientId, clientSecret);
            executor.setConnectionTimeout(connectionTimeout);
            executor.setSocketReadTimeout(socketReadTimeout);
            return new MarketoRestClient(executor);
        }

        public Builder withConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder withSocketReadTimeout(int socketReadTimeout) {
            this.socketReadTimeout = socketReadTimeout;
            return this;
        }
    }
}
