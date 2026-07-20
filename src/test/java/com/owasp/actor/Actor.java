package com.owasp.actor;

import com.microsoft.playwright.APIRequestContext;
import com.owasp.components.NavbarComponent;
import com.owasp.context.TestContext;
import com.owasp.pages.AdministrationPage;
import com.owasp.pages.LoginPage;
import com.owasp.pages.MainPage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A named actor driving the system under test. Steps go through an actor to reach page
 * objects, components, or the backend — never through {@link TestContext} directly — so
 * step definitions read as "the actor does X" regardless of whether X happens in the
 * browser or over the wire. Page/component instances are created lazily and cached for the
 * actor's lifetime since the underlying Page doesn't exist until
 * {@code TestContext#initialize()} runs.
 *
 * <p>Actors are kept unique per name by {@link Cast} — obtain one via {@code Cast#actor()}
 * rather than constructing this directly.
 */
@Getter
@RequiredArgsConstructor
public class Actor {

    private final String name;
    private final TestContext testContext;

    private LoginPage loginPage;
    private MainPage mainPage;
    private NavbarComponent navbar;
    private AdministrationPage administrationPage;

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

    public AdministrationPage administrationPage() {
        if (administrationPage == null) {
            administrationPage = new AdministrationPage(testContext.getPage());
        }
        return administrationPage;
    }

    /** Entry point for scenarios that hit the backend directly, bypassing the UI. */
    public APIRequestContext backend() {
        return testContext.getBrowserContext().request();
    }

    @Override
    public String toString() {
        return "Actor[" + name + "]";
    }
}
