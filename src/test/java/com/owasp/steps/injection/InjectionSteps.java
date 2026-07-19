package com.owasp.steps.injection;

import com.owasp.actor.Actor;
import com.owasp.pages.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for OWASP A03:2021 — Injection scenarios.
 * PicoContainer injects {@link Actor} (same instance as in {@code ScenarioHooks}).
 */
@Slf4j
@RequiredArgsConstructor
public class InjectionSteps {

    private final Actor actor;

    @Given("the application is accessible at the base URL")
    public void theApplicationIsAccessible() {
        log.info("Application accessibility pre-validated by Docker health check");
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        LoginPage loginPage = actor.loginPage();
        loginPage.navigate();
        assertThat(loginPage.isOnLoginPage())
                .as("Navigation to login page failed — current URL: " + loginPage.getCurrentUrl())
                .isTrue();
    }

    @When("I attempt to login with email {string} and password {string}")
    public void iAttemptToLoginWith(String email, String password) {
        log.info("Submitting login form — email: [{}]", email);
        actor.loginPage().loginWith(email, password);
    }

    @Then("I should not be authenticated")
    public void iShouldNotBeAuthenticated() {
        assertThat(actor.mainPage().isOnMainPage())
                .as("VULNERABILITY DETECTED [A03 SQL Injection]: Authentication bypass succeeded. "
                        + "The application should have rejected the payload but redirected to the main page.")
                .isFalse();
    }

    @And("I should see a login error message")
    public void iShouldSeeALoginErrorMessage() {
        assertThat(actor.loginPage().hasFormError())
                .as("Expected a login error message to appear after a rejected credential")
                .isTrue();
    }
}
