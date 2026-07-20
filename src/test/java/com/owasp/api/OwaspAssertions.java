package com.owasp.api;

/**
 * Entry point for fluent assertions on this framework's API response DTOs, alongside every
 * standard AssertJ {@code assertThat} overload — step definitions need only one static import.
 */
public class OwaspAssertions extends org.assertj.core.api.Assertions {

    public static UserRegistrationResponseAssert assertThat(UserRegistrationResponse actual) {
        return UserRegistrationResponseAssert.assertThat(actual);
    }
}
