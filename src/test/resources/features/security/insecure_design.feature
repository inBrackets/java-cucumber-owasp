@security @insecure-design
Feature: A04:2021 - Insecure Design
  As a security tester
  I want to verify that the registration endpoint cannot be used to self-assign administrator privileges
  So that mass assignment vulnerabilities in the account creation flow are detected before reaching production

  # Test philosophy: these tests PASS when the application is secure (privilege escalation blocked).
  # A test FAILURE means a vulnerability was detected and requires remediation.

  Background:
    Given the application is accessible at the base URL

  @mass-assignment @broken-access-control @api @TmsLink=_register_as_a_user_with_administrator_privileges
  Scenario: Registration should not honor a client-supplied administrator role
    When I register a new account requesting the "admin" role
    Then the account should not be granted the requested privileges
