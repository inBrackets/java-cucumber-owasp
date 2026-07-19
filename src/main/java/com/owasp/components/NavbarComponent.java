package com.owasp.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * Reusable component encapsulating the Juice Shop top navigation bar.
 * Composed into pages that need to interact with the nav (e.g., to check auth state or log out).
 */
@Slf4j
public class NavbarComponent {

    private final Locator accountMenuButton;
    private final Locator logoutOption;

    public NavbarComponent(Page page) {
        this.accountMenuButton = page.locator("#navbarAccount");
        this.logoutOption = page.locator("[aria-label='Logout']");
    }

    public boolean isAccountMenuVisible() {
        return accountMenuButton.isVisible();
    }

    public void logout() {
        log.info("Logging out via navbar");
        accountMenuButton.click();
        logoutOption.click();
    }
}
