@security @injection
Feature: A03:2021 - Injection
  As a security tester
  I want to verify that SQL injection attacks on the login form are blocked
  So that authentication bypass vulnerabilities are detected before reaching production

  # Test philosophy: these tests PASS when the application is secure (attack blocked).
  # A test FAILURE means a vulnerability was detected and requires remediation.

  Background:
    Given the application is accessible at the base URL

  @sql-injection @login-bypass @ui @TmsLink=_log_in_with_the_administrators_user_account
  Scenario Outline: SQL injection login bypass should be blocked
    Given I am on the login page
    When I attempt to login with email "<email>" and password "<password>"
    Then I should not be authenticated
    And I should see a login error message

    Examples:
      | email                | password |
      | anything             | anything |
      | ' OR 1=1--           | anything |
      | admin@juice-sh.op'-- | anything |
