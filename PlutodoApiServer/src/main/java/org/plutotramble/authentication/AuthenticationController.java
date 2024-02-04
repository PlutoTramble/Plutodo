package org.plutotramble.authentication;

import jakarta.annotation.security.PermitAll;
import org.plutotramble.authentication.exceptions.EmailAddressAlreadyExistsAuthenticationException;
import org.plutotramble.authentication.exceptions.InvalidEmailAddressException;
import org.plutotramble.authentication.exceptions.InvalidPasswordException;
import org.plutotramble.authentication.exceptions.InvalidUsernameException;
import org.plutotramble.authentication.dto.LoginDTO;
import org.plutotramble.authentication.dto.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/Authentication")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;

    AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Async
    @PermitAll
    @PostMapping(value = "/login")
    public CompletableFuture<ResponseEntity<Object>> login(@RequestBody LoginDTO loginDTO){
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(loginDTO.username, loginDTO.password);
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }

    @Async
    @PermitAll
    @PostMapping(value = "/register")
    public CompletableFuture<ResponseEntity<Object>> Register(@RequestBody RegisterDTO registerDTO){
        try {
            authenticationService.CreateUser(registerDTO);
        }
        catch (ExecutionException | InterruptedException e) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        catch (InvalidPasswordException | EmailAddressAlreadyExistsAuthenticationException |
               InvalidEmailAddressException | InvalidUsernameException e) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }
}
