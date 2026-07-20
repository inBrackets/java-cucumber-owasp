package com.owasp.api;

import org.assertj.core.api.AbstractObjectAssert;

import java.util.Objects;

/**
 * Fluent AssertJ assertions for {@link UserRegistrationResponse}.
 */
public class UserRegistrationResponseAssert
        extends AbstractObjectAssert<UserRegistrationResponseAssert, UserRegistrationResponse> {

    private UserRegistrationResponseAssert(UserRegistrationResponse actual) {
        super(actual, UserRegistrationResponseAssert.class);
    }

    public static UserRegistrationResponseAssert assertThat(UserRegistrationResponse actual) {
        return new UserRegistrationResponseAssert(actual);
    }

    /**
     * Fails with a vulnerability report if the account was granted {@code forbiddenRole}.
     */
    public UserRegistrationResponseAssert doesNotHaveRole(String forbiddenRole) {
        isNotNull();
        String actualRole = actual.data().role();
        if (Objects.equals(actualRole, forbiddenRole)) {
            failWithMessage("""
                            VULNERABILITY DETECTED [A04 Insecure Design - Mass Assignment]: expected the \
                            registration endpoint to ignore the client-supplied "role" field, but the \
                            account was created with role "%s" — response: %s""",
                    actualRole, actual);
        }
        return this;
    }
}
