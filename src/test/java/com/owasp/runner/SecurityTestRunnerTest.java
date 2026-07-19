package com.owasp.runner;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * JUnit Platform Suite entry point. Cucumber engine configuration (glue, plugins, tags)
 * lives in {@code src/test/resources/junit-platform.properties} to keep this class minimal.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class SecurityTestRunnerTest {
}
