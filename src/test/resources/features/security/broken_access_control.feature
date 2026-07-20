@security @broken-access-control
Feature: A01:2021 - Broken Access Control
  As a security tester
  I want to verify that confidential documents cannot be retrieved without authentication
  So that unauthorized disclosure of sensitive business files is detected before reaching production

  # Test philosophy: these tests PASS when the application is secure (access blocked).
  # A test FAILURE means a vulnerability was detected and requires remediation.

  Background:
    Given the application is accessible at the base URL

  @sensitive-data-exposure @api @TmsLink=_access_a_confidential_document
  Scenario: Confidential acquisitions document should not be accessible without authentication
    When I request the "/ftp/acquisitions.md" file without authentication
    Then access to the confidential document should be denied

  # Positive-path control: complements the denial above by confirming a legitimately
  # authenticated administrator can still reach the restricted section.
  @administration-access @ui @TmsLink=_access_the_administration_section_of_the_store
  Scenario: Administrator should be able to access the administration section
    Given I am on the login page
    When I attempt to login with email "admin@juice-sh.op" and password "admin123"
    Then I should be authenticated
    When I navigate to the administration section
    Then I should see the administration page

  # The chatbot's tool-call debug view is meant for admins only, but that check is only
  # enforced client-side via the "show_tool_calls" cookie — the server includes the data
  # regardless of who asks.
  @ai-debugging @api @TmsLink=_reveal_some_behind_the_scenes_information_on_the_chatbot_as_a_non_admin_user
  Scenario: Chatbot tool-call debugging information should not be exposed to a non-admin user
    Given I have registered a new customer account
    And I have set the "show_tool_calls" debug cookie to "true"
    When I ask the chatbot "Do you have any apples in stock?"
    Then the chatbot response should not reveal tool call debugging information
