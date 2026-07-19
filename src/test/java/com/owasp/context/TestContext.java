package com.owasp.context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.owasp.browser.PlaywrightManager;
import com.owasp.config.FrameworkConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        browserContext.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = browserContext.newPage();
        log.debug("Test context initialised — new BrowserContext and Page created, tracing started");
    }

    public byte[] takeScreenshot() {
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }

    /**
     * Stops Playwright tracing and returns the recorded trace — every UI interaction and
     * network request made during the scenario — as a zip, viewable with Playwright's trace
     * viewer.
     */
    public byte[] stopTracing() {
        try {
            Path tracePath = Files.createTempFile("playwright-trace-", ".zip");
            try {
                browserContext.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
                return Files.readAllBytes(tracePath);
            } finally {
                Files.deleteIfExists(tracePath);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Called from {@code ScenarioHooks#afterScenario} to release resources. */
    public void close() {
        if (browserContext != null) {
            browserContext.close(); // disposes owned pages automatically
        }
        log.debug("Test context closed");
    }
}
