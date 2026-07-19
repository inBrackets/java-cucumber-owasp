package com.owasp.actor;

import com.owasp.context.TestContext;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-scenario registry of {@link Actor}s: asking for the same name twice within a scenario
 * always returns the same instance. A fresh {@code Cast} is created for each scenario
 * (PicoContainer instantiates one alongside {@link TestContext}), so actors never leak
 * between scenarios.
 *
 * <p>Today every scenario only needs one actor — {@link #actor()} hands back that default —
 * but scenarios that need several (e.g. an attacker and a victim) can ask for each by name
 * via {@link #actor(String)} without changing how any of this works.
 */
@RequiredArgsConstructor
public class Cast {

    public static final String DEFAULT = "Default";

    private final TestContext testContext;
    private final Map<String, Actor> actors = new ConcurrentHashMap<>();

    public Actor actor(String name) {
        return actors.computeIfAbsent(name, n -> new Actor(n, testContext));
    }

    public Actor actor() {
        return actor(DEFAULT);
    }
}
