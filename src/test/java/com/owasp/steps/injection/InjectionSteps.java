package com.owasp.steps.injection;

import com.microsoft.playwright.options.LoadState;
import com.owasp.context.TestContext;
import com.owasp.pages.LoginPage;
import com.owasp.pages.MainPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for OWASP A03:2021 — Injection scenarios.
 * PicoContainer injects {@link TestContext} (same instance as in {@code ScenarioHooks}).
 */
@Slf4j
@RequiredArgsConstructor
public class InjectionSteps {

    private final TestContext testContext;

    private LoginPage loginPage;
    private MainPage mainPage;

    @Given("the application is accessible at the base URL")
    public void theApplicationIsAccessible() {
        log.info("Application accessibility pre-validated by Docker health check");
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage = new LoginPage(testContext.getPage());
        mainPage = new MainPage(testContext.getPage());
        loginPage.navigate();
        assertThat(loginPage.isOnLoginPage())
                .as("Navigation to login page failed — current URL: " + loginPage.getCurrentUrl())
                .isTrue();
    }

    @When("I attempt to login with email {string} and password {string}")
    public void iAttemptToLoginWith(String email, String password) {
        log.info("Submitting login form — email: [{}]", email);
        loginPage.loginWith(email, password);
        testContext.getPage().waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Then("I should not be authenticated")
    public void iShouldNotBeAuthenticated() {
        assertThat(mainPage.isOnMainPage())
                .as("VULNERABILITY DETECTED [A03 SQL Injection]: Authentication bypass succeeded. "
                        + "The application should have rejected the payload but redirected to the main page.")
                .isFalse();
    }

    @And("I should see a login error message")
    public void iShouldSeeALoginErrorMessage() {
        assertThat(loginPage.hasFormError())
                .as("Expected a login error message to appear after a rejected credential")
                .isTrue();
    }
}
