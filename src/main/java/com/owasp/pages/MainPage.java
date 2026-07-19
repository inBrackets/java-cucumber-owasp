package com.owasp.pages;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainPage extends BasePage {

    public MainPage(Page page) {
        super(page);
    }

    @Override
    public void navigate() {
        navigateTo("/#/");
    }

    /**
     * Returns true when the browser has been redirected away from the login page to the
     * main application shell — the signal that authentication succeeded.
     */
    public boolean isOnMainPage() {
        return !getCurrentUrl().contains("/#/login");
    }
}
