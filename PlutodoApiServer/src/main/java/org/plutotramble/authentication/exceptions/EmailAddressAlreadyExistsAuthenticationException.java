package org.plutotramble.authentication.exceptions;

import org.springframework.security.core.AuthenticationException;

public class EmailAddressAlreadyExistsAuthenticationException extends AuthenticationException {
    public EmailAddressAlreadyExistsAuthenticationException(final String msg) {
        super(msg);
    }
}
