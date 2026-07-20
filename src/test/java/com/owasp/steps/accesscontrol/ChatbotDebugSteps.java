package com.owasp.steps.accesscontrol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.owasp.actor.Cast;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for OWASP A01:2021 — Broken Access Control: the chatbot's tool-call debug
 * view is meant for admins only, but the app only enforces that client-side via a cookie the
 * server never actually checks before including the data in its response.
 */
@Slf4j
@RequiredArgsConstructor
public class ChatbotDebugSteps {

    private static final ObjectMapper JSON = new ObjectMapper();

    private final Cast cast;

    private String authToken;
    private APIResponse chatResponse;

    @Given("I have registered a new customer account")
    public void iHaveRegisteredANewCustomerAccount() throws IOException {
        String email = "pentest-" + UUID.randomUUID() + "@test.local";
        String password = "P@ssw0rd123!";

        APIRequestContext backend = cast.actor().backend();
        backend.post("/api/Users", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(Map.of("email", email, "password", password)));

        APIResponse loginResponse = backend.post("/rest/user/login", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(Map.of("email", email, "password", password)));

        authToken = JSON.readTree(loginResponse.text()).path("authentication").path("token").asText();
    }

    @And("I have set the {string} debug cookie to {string}")
    public void iHaveSetTheDebugCookieTo(String cookieName, String cookieValue) {
        cast.actor().setCookie(cookieName, cookieValue);
    }

    @When("I ask the chatbot {string}")
    public void iAskTheChatbot(String message) {
        APIRequestContext backend = cast.actor().backend();
        chatResponse = backend.post("/rest/chat", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer " + authToken)
                .setData(Map.of("messages", List.of(Map.of("role", "user", "content", message)))));
    }

    @Then("the chatbot response should not reveal tool call debugging information")
    public void theChatbotResponseShouldNotRevealToolCallDebuggingInformation() {
        String body = chatResponse.text();
        assertThat(body)
                .as("""
                        VULNERABILITY DETECTED [A01 Broken Access Control - AI Debugging]: expected tool-call \
                        debugging information to be withheld from a non-admin customer, but the chatbot API \
                        response included it regardless of role — response: %s""", body)
                .doesNotContain("\"tool_calls\"");
    }
}
