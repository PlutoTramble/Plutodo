package org.plutotramble.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class NextcloudException extends AuthenticationException {
    public NextcloudException(String message) {
        super(message);
    }
}
