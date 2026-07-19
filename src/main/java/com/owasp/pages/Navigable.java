package com.owasp.pages;

/**
 * Interface Segregation: separates navigation capability from page state queries.
 * Every page that can be navigated to implements this contract.
 */
public interface Navigable {
    void navigate();
}
