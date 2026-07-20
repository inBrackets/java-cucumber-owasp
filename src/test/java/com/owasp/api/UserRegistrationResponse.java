package com.owasp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Partial view of the {@code POST /api/Users} response body — only the fields security scenarios assert on.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserRegistrationResponse(Data data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(String email, String role) {
    }
}
