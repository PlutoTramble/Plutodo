package org.plutotramble.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class InvalidEmailAddressException extends AuthenticationException {
    public InvalidEmailAddressException(final String msg) {
        super(msg);
    }
}
