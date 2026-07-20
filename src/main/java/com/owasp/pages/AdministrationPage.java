package com.owasp.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdministrationPage extends BasePage {

    private final Locator registeredUsersHeading;

    public AdministrationPage(Page page) {
        super(page);
        this.registeredUsersHeading = page.locator("text=Registered Users");
    }

    @Override
    public void navigate() {
        navigateTo("/#/administration");
    }

    /**
     * True once the user management table has rendered — only happens for an authenticated session.
     */
    public boolean isUserListVisible() {
        return registeredUsersHeading.isVisible();
    }
}
