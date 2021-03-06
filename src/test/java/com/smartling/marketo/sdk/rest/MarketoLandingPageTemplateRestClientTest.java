package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplate;
import com.smartling.marketo.sdk.domain.landingpagetemplate.LandingPageTemplateContentItem;
import com.smartling.marketo.sdk.rest.command.landingpagetemplate.GetLandingPageTemplateById;
import com.smartling.marketo.sdk.rest.command.landingpagetemplate.GetLandingPageTemplateContent;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;

@RunWith(MockitoJUnitRunner.class)
public class MarketoLandingPageTemplateRestClientTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoLandingPageTemplateRestClient testedInstance;

    @Test
    public void shouldGetLandingPageById() throws Exception {
        LandingPageTemplate landingPageTemplate = new LandingPageTemplate();
        given(executor.execute(isA(GetLandingPageTemplateById.class))).willReturn(Collections.singletonList(landingPageTemplate));

        LandingPageTemplate result = testedInstance.getLandingPageTemplateById(42);

        assertThat(result).isEqualTo(landingPageTemplate);
    }

    @Test
    public void shouldGetLandingPageByIdAndStatus() throws Exception {
        LandingPageTemplate landingPageTemplate = new LandingPageTemplate();
        given(executor.execute(isA(GetLandingPageTemplateById.class))).willReturn(Collections.singletonList(landingPageTemplate));

        LandingPageTemplate result = testedInstance.getLandingPageTemplateById(42, Status.DRAFT);

        assertThat(result).isEqualTo(landingPageTemplate);
    }

    @Test
    public void shouldThrowMarketoApiExceptionIfLandingPageNotFound() throws Exception
    {
        int nonExistingId = 42;

        given(executor.execute(isA(GetLandingPageTemplateById.class))).willReturn(null);

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("LandingPageTemplate[id = 42] not found");

        testedInstance.getLandingPageTemplateById(nonExistingId);
    }

    @Test
    public void shouldGetLandingPageContent() throws Exception {
        LandingPageTemplateContentItem contentItem = new LandingPageTemplateContentItem();
        given(executor.execute(isA(GetLandingPageTemplateContent.class))).willReturn(Collections.singletonList(contentItem));

        List<LandingPageTemplateContentItem> result = testedInstance.getLandingPageTemplateContent(42);

        assertThat(result).contains(contentItem);
    }
}