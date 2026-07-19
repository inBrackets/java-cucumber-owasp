@security @misconfiguration
Feature: A05:2021 - Security Misconfiguration
  As a security tester
  I want to verify that the server's support log directory cannot be browsed without authentication
  So that sensitive access log disclosure vulnerabilities are detected before reaching production

  # Test philosophy: these tests PASS when the application is secure (access blocked).
  # A test FAILURE means a vulnerability was detected and requires remediation.

  Background:
    Given the application is accessible at the base URL

  @broken-access-control @sensitive-data-exposure @api @TmsLink=_gain_access_to_any_access_log_file_of_the_server
  Scenario: Support log directory should not be browsable without authentication
    When I request the "/support/logs/" directory without authentication
    Then access to the directory listing should be denied
    And no access log file should be disclosed in the response
