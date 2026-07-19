package com.owasp.steps.misconfiguration;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.owasp.actor.Cast;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for OWASP A05:2021 — Security Misconfiguration scenarios.
 * Drives the backend directly via {@link Cast#actor()}'s {@code backend()} — no browser
 * needed to check whether a directory listing is exposed.
 */
@Slf4j
@RequiredArgsConstructor
public class AccessLogSteps {

    private final Cast cast;

    private APIResponse response;

    @When("I request the {string} directory without authentication")
    public void iRequestDirectoryWithoutAuthentication(String path) {
        APIRequestContext backend = cast.actor().backend();
        response = backend.get(path);
    }

    @Then("access to the directory listing should be denied")
    public void accessToTheDirectoryListingShouldBeDenied() {
        assertThat(response.ok())
                .as("VULNERABILITY DETECTED [A05 Security Misconfiguration]: expected an unauthenticated "
                        + "request to be rejected but got " + response.status()
                        + " — the support log directory is publicly browsable")
                .isFalse();
    }
}
