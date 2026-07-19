package com.owasp.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import lombok.extern.slf4j.Slf4j;

/**
 * Template Method pattern: defines the skeleton of page navigation ({@link #goto}) while
 * leaving the route ({@link #navigate}) to each concrete page. Handles cross-cutting concerns
 * shared by all pages: load waiting and overlay dismissal.
 */
@Slf4j
public abstract class BasePage implements Navigable {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    /**
     * Navigate to the given path relative to {@code baseUrl}, then wait for the SPA
     * to settle and dismiss any one-time overlays (welcome banner, cookie consent).
     */
    protected void navigateTo(String path) {
        log.debug("Navigating to: {}", path);
        page.navigate(path);
        waitForPageLoad();
        dismissOverlays();
    }

    protected void waitForPageLoad() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    private void dismissOverlays() {
        for (String selector : new String[]{
                "button[aria-label='Close Welcome Banner']",
                "a[aria-label='dismiss cookie message']"}) {
            Locator overlay = page.locator(selector);
            if (overlay.isVisible()) {
                try {
                    overlay.click(new Locator.ClickOptions().setTimeout(500));
                } catch (Exception ignored) {}
            }
        }
    }

    public String getTitle() {
        return page.title();
    }

    public String getCurrentUrl() {
        return page.url();
    }
}
