package com.owasp.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainPage extends BasePage {

    public MainPage(Page page) {
        super(page);
    }

    @Override
    public void navigate() {
        navigateTo("/#/");
    }

    /**
     * Returns true when the browser has been redirected away from the login page to the
     * main application shell — the signal that authentication succeeded.
     */
    public boolean isOnMainPage() {
        return !getCurrentUrl().contains("/#/login");
    }

    /**
     * Waits for the SPA to redirect away from the login route after a submitted login — the
     * redirect can lag slightly behind the login request settling. Returns false if no
     * redirect happens within the timeout, e.g. because the login was rejected.
     */
    public boolean waitForAuthenticationRedirect() {
        try {
            page.waitForURL(url -> !url.contains("/#/login"), new Page.WaitForURLOptions().setTimeout(5000));
            return true;
        } catch (PlaywrightException e) {
            return false;
        }
    }
}
