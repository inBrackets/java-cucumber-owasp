package com.owasp.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
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
        this.formError = page.locator("mat-error");
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

    /** Convenience: fills credentials and submits in one call. */
    public void loginWith(String email, String password) {
        log.debug("Attempting login with email: {}", email);
        fillEmail(email);
        fillPassword(password);
        submit();
    }

    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("/#/login");
    }

    public boolean hasFormError() {
        return formError.count() > 0;
    }

    public String getFormError() {
        return formError.first().textContent().trim();
    }

    public boolean isLoginButtonEnabled() {
        return loginButton.isEnabled();
    }
}
