package org.plutotramble.Exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidPasswordException extends AuthenticationException {
    public InvalidPasswordException(final String msg) {
        super(msg);
    }
}
