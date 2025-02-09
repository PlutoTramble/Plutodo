package org.plutotramble.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class InvalidUsernameException extends AuthenticationException {
    public InvalidUsernameException(final String msg) {
        super(msg);
    }
}
