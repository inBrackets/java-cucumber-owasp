package com.owasp.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Singleton that owns the {@link Playwright} and {@link Browser} lifecycle for the entire test run.
 * The browser is shared across scenarios; each scenario gets its own {@code BrowserContext} and
 * {@code Page} (managed by {@code TestContext}).
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlaywrightManager {

    private static Playwright playwright;
    private static Browser browser;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(PlaywrightManager::shutdown, "playwright-shutdown"));
    }

    public static synchronized Browser getBrowser() {
        if (browser == null) {
            playwright = Playwright.create();
            browser = BrowserFactory.create(playwright);
            log.info("Playwright browser started");
        }
        return browser;
    }

    private static synchronized void shutdown() {
        if (browser != null) {
            browser.close();
            browser = null;
            log.info("Browser closed");
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }
}
