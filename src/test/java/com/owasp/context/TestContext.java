package com.owasp.context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.owasp.browser.PlaywrightManager;
import com.owasp.config.FrameworkConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds per-scenario Playwright state (BrowserContext + Page) and is injected into steps
 * and hooks by PicoContainer. A new instance is created for each Cucumber scenario,
 * providing full test isolation without restarting the browser.
 */
@Slf4j
@Getter
public class TestContext {

    private BrowserContext browserContext;
    private Page page;

    /** Called from {@code ScenarioHooks#beforeScenario} to set up a fresh context. */
    public void initialize() {
        FrameworkConfig cfg = FrameworkConfig.getInstance();
        Browser browser = PlaywrightManager.getBrowser();
        browserContext = browser.newContext(new Browser.NewContextOptions()
                .setBaseURL(cfg.getBaseUrl()));
        browserContext.setDefaultTimeout(cfg.getDefaultTimeout());
        page = browserContext.newPage();
        log.debug("Test context initialised — new BrowserContext and Page created");
    }

    public byte[] takeScreenshot() {
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }

    /** Called from {@code ScenarioHooks#afterScenario} to release resources. */
    public void close() {
        if (browserContext != null) {
            browserContext.close(); // disposes owned pages automatically
        }
        log.debug("Test context closed");
    }
}
