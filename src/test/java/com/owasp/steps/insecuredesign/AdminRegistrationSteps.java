package com.owasp.steps.insecuredesign;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.owasp.actor.Cast;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for OWASP A04:2021 — Insecure Design scenarios.
 * Drives the backend directly via {@link Cast#actor()}'s {@code backend()} — registration is
 * a pure API concern, no browser needed.
 */
@Slf4j
@RequiredArgsConstructor
public class AdminRegistrationSteps {

    private static final ObjectMapper JSON = new ObjectMapper();

    private final Cast cast;

    private APIResponse response;
    private String requestedRole;

    @When("I register a new account requesting the {string} role")
    public void iRegisterANewAccountRequestingTheRole(String role) {
        requestedRole = role;
        Map<String, Object> payload = Map.of(
                "email", "pentest-" + UUID.randomUUID() + "@test.local",
                "password", "P@ssw0rd123!",
                "role", role);

        APIRequestContext backend = cast.actor().backend();
        response = backend.post("/api/Users", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(payload));
    }

    @Then("the account should not be granted the requested privileges")
    public void theAccountShouldNotBeGrantedTheRequestedPrivileges() throws IOException {
        String raw = response.text();
        String grantedRole = JSON.readTree(raw).path("data").path("role").asText();

        assertThat(grantedRole)
                .as("VULNERABILITY DETECTED [A04 Insecure Design - Mass Assignment]: expected the "
                        + "registration endpoint to ignore the client-supplied \"role\" field, but the "
                        + "account was created with role \"" + grantedRole + "\" — response: " + raw)
                .isNotEqualTo(requestedRole);
    }
}
