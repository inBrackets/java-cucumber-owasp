@security @injection
Feature: A03:2021 - Injection
  As a security tester
  I want to verify that SQL injection attacks on the login form are blocked
  So that authentication bypass vulnerabilities are detected before reaching production

  # Test philosophy: these tests PASS when the application is secure (attack blocked).
  # A test FAILURE means a vulnerability was detected and requires remediation.

  Background:
    Given the application is accessible at the base URL

  @sql-injection @login-bypass @ui
  Scenario: SQL injection login bypass should be blocked
    Given I am on the login page
    When I attempt to login with email "' OR 1=1--" and password "anything"
    Then I should not be authenticated
    And I should see a login error message
