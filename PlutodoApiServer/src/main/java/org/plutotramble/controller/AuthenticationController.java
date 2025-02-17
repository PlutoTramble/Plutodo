package org.plutotramble.controller;

import jakarta.annotation.security.PermitAll;
import org.plutotramble.dto.ErrorMessageDTO;
import org.plutotramble.dto.LoginDTO;
import org.plutotramble.dto.RegisterDTO;
import org.plutotramble.service.AuthenticationService;
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

    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;

    AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @Async
    @PermitAll
    @PostMapping(value = "/login")
    public CompletableFuture<ResponseEntity<Object>> login(@RequestBody LoginDTO loginDTO){
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(loginDTO.username, loginDTO.password);

        if(!authReq.isAuthenticated()) {
            ErrorMessageDTO errorMessage = new ErrorMessageDTO("Invalid username or password.");
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage));
        }

        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }

    @Async
    @PermitAll
    @PostMapping(value = "/register")
    public CompletableFuture<ResponseEntity<Object>> register(@RequestBody RegisterDTO registerDTO) throws ExecutionException, InterruptedException {
        try {
            authenticationService.CreateUser(registerDTO);
        }
        catch (Exception e) {
            ErrorMessageDTO errorMessage = new ErrorMessageDTO(e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage));
        }

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.username = registerDTO.username;
        loginDTO.password = registerDTO.password;

        return CompletableFuture.completedFuture(login(loginDTO).get());
    }
}
