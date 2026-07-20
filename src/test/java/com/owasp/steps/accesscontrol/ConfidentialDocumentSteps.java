package com.owasp.steps.accesscontrol;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.owasp.actor.Cast;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for OWASP A01:2021 — Broken Access Control scenarios.
 * Drives the backend directly via {@link Cast#actor()}'s {@code backend()} — no browser
 * needed to check whether a file is reachable without authentication.
 */
@Slf4j
@RequiredArgsConstructor
public class ConfidentialDocumentSteps {

    private final Cast cast;

    private APIResponse response;

    @When("I request the {string} file without authentication")
    public void iRequestTheFileWithoutAuthentication(String path) {
        APIRequestContext backend = cast.actor().backend();
        response = backend.get(path);
    }

    @Then("access to the confidential document should be denied")
    public void accessToTheConfidentialDocumentShouldBeDenied() {
        assertThat(response.ok())
                .as("""
                                VULNERABILITY DETECTED [A01 Broken Access Control]: expected an unauthenticated \
                                request to be rejected but got %s — the confidential document is publicly readable""",
                        response.status())
                .isFalse();
    }
}
