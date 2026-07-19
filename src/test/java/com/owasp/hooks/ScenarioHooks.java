package com.owasp.hooks;

import com.owasp.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Cucumber lifecycle hooks. PicoContainer injects the same {@link TestContext} instance that
 * is shared with all step-definition classes within the same scenario.
 */
@Slf4j
@RequiredArgsConstructor
public class ScenarioHooks {

    private final TestContext testContext;

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("▶ Starting: {}", scenario.getName());
        testContext.initialize();
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            log.warn("✖ Failed: {} — capturing screenshot", scenario.getName());
            scenario.attach(testContext.takeScreenshot(), "image/png", "Screenshot on Failure");
        }
        scenario.attach(testContext.stopTracing(), "application/vnd.allure.playwright-trace", "Playwright Trace");
        testContext.close();
        log.info("■ Finished: {} [{}]", scenario.getName(), scenario.getStatus());
    }
}
