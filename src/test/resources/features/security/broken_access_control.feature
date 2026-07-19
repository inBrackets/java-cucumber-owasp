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
