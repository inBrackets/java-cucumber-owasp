package com.owasp.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.owasp.config.FrameworkConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory that creates a configured {@link Browser} from a live {@link Playwright} instance.
 * Selects the browser engine from {@link FrameworkConfig} — defaults to Chromium.
 * Open/Closed: add new browser types here without touching callers.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BrowserFactory {

    public static Browser create(Playwright playwright) {
        FrameworkConfig cfg = FrameworkConfig.getInstance();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(cfg.isHeadless())
                .setSlowMo(cfg.getSlowMo());

        log.info("Launching {}", cfg.getBrowserType());
        return switch (cfg.getBrowserType().toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit"  -> playwright.webkit().launch(options);
            default        -> playwright.chromium().launch(options);
        };
    }
}
