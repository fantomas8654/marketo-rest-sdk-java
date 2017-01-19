package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoLandingPageTemplateClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate;
import com.smartling.marketo.sdk.rest.command.landingpagetemplate.GetLandingPageTemplateById;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;

import java.util.List;

public class MarketoLandingPageTemplateRestClient implements MarketoLandingPageTemplateClient {

    private final HttpCommandExecutor httpCommandExecutor;

    public MarketoLandingPageTemplateRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public LandingPageTemplate getLandingPageTemplateById(int id, Status status) throws MarketoApiException {
        List<LandingPageTemplate> execute = httpCommandExecutor.execute(new GetLandingPageTemplateById(id, status));
        if (execute != null && !execute.isEmpty()) {
            return execute.get(0);
        } else {
            throw new MarketoApiException(String.format("LandingPageTemplate[id = %d] not found", id));
        }
    }

    @Override
    public LandingPageTemplate getLandingPageTemplateById(int id) throws MarketoApiException {
        return getLandingPageTemplateById(id, null);
    }
}
