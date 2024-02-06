package org.plutotramble.authentication.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidEmailAddressException extends AuthenticationException {
    public InvalidEmailAddressException(final String msg) {
        super(msg);
    }
}
