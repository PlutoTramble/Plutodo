package org.plutotramble.controller;

import org.plutotramble.exception.InvalidItemPropertyException;
import org.plutotramble.exception.ItemNotFoundException;
import org.plutotramble.exception.authentication.EmailAddressAlreadyExistsAuthenticationException;
import org.plutotramble.exception.authentication.InvalidEmailAddressException;
import org.plutotramble.exception.authentication.InvalidPasswordException;
import org.plutotramble.exception.authentication.InvalidUsernameException;
import org.plutotramble.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.CompletableFuture;

@ControllerAdvice
public class ControllerExceptionHandler {

    @Async
    @ExceptionHandler(value = { InvalidItemPropertyException.class,
                                InvalidPasswordException.class,
                                EmailAddressAlreadyExistsAuthenticationException.class,
                                InvalidEmailAddressException.class,
                                InvalidUsernameException.class})
    protected CompletableFuture<ResponseEntity<Object>> handleInvalidItem(Exception e) {
        return CompletableFuture.completedFuture(new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST));
    }

    @Async
    @ExceptionHandler(value = { ItemNotFoundException.class })
    protected CompletableFuture<ResponseEntity<Object>> handleItemNotFount(Exception e) {
        return CompletableFuture.completedFuture(new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND));
    }
}
