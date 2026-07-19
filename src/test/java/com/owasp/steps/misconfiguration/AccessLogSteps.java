package com.owasp.steps.misconfiguration;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.owasp.actor.Cast;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for OWASP A05:2021 — Security Misconfiguration scenarios.
 * Drives the backend directly via {@link Cast#actor()}'s {@code backend()} — no browser
 * needed to check whether a directory listing is exposed.
 */
@Slf4j
@RequiredArgsConstructor
public class AccessLogSteps {

    private static final Pattern ACCESS_LOG_LINK = Pattern.compile("href=\"[^\"]*access\\.log[^\"]*\"");

    private final Cast cast;

    private APIResponse response;

    @When("I request the {string} directory without authentication")
    public void iRequestDirectoryWithoutAuthentication(String path) {
        APIRequestContext backend = cast.actor().backend();
        response = backend.get(path);
    }

    @Then("access to the directory listing should be denied")
    public void accessToTheDirectoryListingShouldBeDenied() {
        assertThat(response.status())
                .as("VULNERABILITY DETECTED [A05 Security Misconfiguration]: expected an unauthenticated "
                        + "request to be rejected (401/403) but got " + response.status()
                        + " — the support log directory is publicly browsable")
                .isIn(401, 403);
    }

    @And("no access log file should be disclosed in the response")
    public void noAccessLogFileShouldBeDisclosedInTheResponse() {
        assertThat(ACCESS_LOG_LINK.matcher(response.text()).find())
                .as("VULNERABILITY DETECTED [A05 Security Misconfiguration]: response body references "
                        + "an access.log file that should not be reachable without authentication")
                .isFalse();
    }
}
