package com.smartling.marketo.sdk.transport;

import com.fasterxml.jackson.annotation.JsonProperty;

class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("error_description")
    private String errorDescription;

    public String getAccessToken() {
        return accessToken;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
