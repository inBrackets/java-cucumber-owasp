package com.owasp.hooks;

import com.owasp.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
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

    /**
     * Captures the screenshot right after the failing step, as its own step-level attachment,
     * rather than in {@code @After} — an attachment made there lands on the tear-down fixture
     * instead of the test body, where it's easy to miss in the Allure report.
     */
    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            log.warn("✖ Failed: {} — capturing screenshot", scenario.getName());
            scenario.attach(testContext.takeScreenshot(), "image/png", "Screenshot on Failure");
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        scenario.attach(testContext.stopTracing(), "application/zip", "Playwright Trace");
        testContext.close();
        log.info("■ Finished: {} [{}]", scenario.getName(), scenario.getStatus());
    }
}
