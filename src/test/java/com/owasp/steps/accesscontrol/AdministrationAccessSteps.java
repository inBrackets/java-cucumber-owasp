package com.owasp.steps.accesscontrol;

import com.owasp.actor.Cast;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the "Access the administration section of the store" scenario under
 * OWASP A01:2021 — Broken Access Control.
 */
@Slf4j
@RequiredArgsConstructor
public class AdministrationAccessSteps {

    private final Cast cast;

    @Then("I should be authenticated")
    public void iShouldBeAuthenticated() {
        assertThat(cast.actor().mainPage().waitForAuthenticationRedirect())
                .as("Expected login with valid credentials to succeed and land on the main page")
                .isTrue();
    }

    @When("I navigate to the administration section")
    public void iNavigateToTheAdministrationSection() {
        cast.actor().administrationPage().navigate();
    }

    @Then("I should see the administration page")
    public void iShouldSeeTheAdministrationPage() {
        assertThat(cast.actor().administrationPage().isUserListVisible())
                .as("""
                        Expected the authenticated administrator to see the user management table \
                        on the administration page""")
                .isTrue();
    }
}
