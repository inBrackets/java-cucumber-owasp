package com.owasp.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginPage extends BasePage {

    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator formError;

    public LoginPage(Page page) {
        super(page);
        this.emailInput = page.locator("#email");
        this.passwordInput = page.locator("#password");
        this.loginButton = page.locator("#loginButton");
        this.formError = page.locator("div.error");
    }

    @Override
    public void navigate() {
        navigateTo("/#/login");
    }

    public void fillEmail(String email) {
        emailInput.fill(email);
    }

    public void fillPassword(String password) {
        passwordInput.fill(password);
    }

    public void submit() {
        loginButton.click();
    }

    /** Convenience: fills credentials, submits, and waits for the resulting navigation/XHR to settle. */
    public void loginWith(String email, String password) {
        log.debug("Attempting login with email: {}", email);
        fillEmail(email);
        fillPassword(password);
        submit();
        waitForPageLoad();
    }

    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("/#/login");
    }

    /** Waits for the error banner to render — it can appear briefly after the login request settles. */
    public boolean hasFormError() {
        try {
            formError.waitFor();
            return true;
        } catch (PlaywrightException e) {
            return false;
        }
    }

    public String getFormError() {
        return formError.first().textContent().trim();
    }

    public boolean isLoginButtonEnabled() {
        return loginButton.isEnabled();
    }
}
