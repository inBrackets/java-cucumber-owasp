package com.owasp.actor;

import com.microsoft.playwright.APIRequestContext;
import com.owasp.components.NavbarComponent;
import com.owasp.context.TestContext;
import com.owasp.pages.LoginPage;
import com.owasp.pages.MainPage;
import lombok.RequiredArgsConstructor;

/**
 * The actor driving the system under test. Steps go through the actor to reach page objects,
 * components, or the backend — never through {@link TestContext} directly — so step
 * definitions read as "the actor does X" regardless of whether X happens in the browser or
 * over the wire. Page/component instances are created lazily and cached per scenario since
 * the underlying Page doesn't exist until {@code TestContext#initialize()} runs.
 */
@RequiredArgsConstructor
public class Actor {

    private final TestContext testContext;

    private LoginPage loginPage;
    private MainPage mainPage;
    private NavbarComponent navbar;

    public LoginPage loginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(testContext.getPage());
        }
        return loginPage;
    }

    public MainPage mainPage() {
        if (mainPage == null) {
            mainPage = new MainPage(testContext.getPage());
        }
        return mainPage;
    }

    public NavbarComponent navbar() {
        if (navbar == null) {
            navbar = new NavbarComponent(testContext.getPage());
        }
        return navbar;
    }

    /** Entry point for scenarios that hit the backend directly, bypassing the UI. */
    public APIRequestContext backend() {
        return testContext.getBrowserContext().request();
    }
}
