package com.owasp.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Getter
public final class FrameworkConfig {

    private static final Properties JUNIT_PLATFORM_PROPERTIES = loadJunitPlatformProperties();
    private static final FrameworkConfig INSTANCE = new FrameworkConfig();

    private final String baseUrl;
    private final boolean headless;
    private final int defaultTimeout;
    private final double slowMo;
    private final String browserType;

    private FrameworkConfig() {
        this.baseUrl = resolve("BASE_URL", "base.url", "http://localhost:3000");
        this.headless = Boolean.parseBoolean(resolve("HEADLESS", "headless", "true"));
        this.defaultTimeout = Integer.parseInt(resolve("TIMEOUT_MS", "timeout.ms", "30000"));
        this.slowMo = Double.parseDouble(resolve("SLOW_MO", "slow.mo", "0"));
        this.browserType = resolve("BROWSER", "browser", "chromium");
        log.info("Config: baseUrl={}, browser={}, headless={}, timeout={}ms",
                baseUrl, browserType, headless, defaultTimeout);
    }

    public static FrameworkConfig getInstance() {
        return INSTANCE;
    }

    private static String resolve(String envVar, String sysProp, String defaultValue) {
        String env = System.getenv(envVar);
        if (env != null && !env.isBlank()) return env;
        String prop = System.getProperty(sysProp);
        if (prop != null && !prop.isBlank()) return prop;
        String fileProp = JUNIT_PLATFORM_PROPERTIES.getProperty(sysProp);
        if (fileProp != null && !fileProp.isBlank()) return fileProp;
        return defaultValue;
    }

    private static Properties loadJunitPlatformProperties() {
        Properties props = new Properties();
        try (InputStream in = FrameworkConfig.class.getClassLoader().getResourceAsStream("junit-platform.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            log.warn("Could not load junit-platform.properties", e);
        }
        return props;
    }
}
