package org.plutotramble.authentication;

import jakarta.annotation.security.PermitAll;
import org.plutotramble.authentication.dto.LoginDTO;
import org.plutotramble.authentication.dto.RegisterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }

    @Async
    @PermitAll
    @PostMapping(value = "/register")
    public CompletableFuture<ResponseEntity<Object>> Register(@RequestBody RegisterDTO registerDTO) throws ExecutionException, InterruptedException {
        authenticationService.CreateUser(registerDTO);

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(registerDTO.username, registerDTO.password);
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }

    @Async
    @GetMapping(value = "/getUsername")
    CompletableFuture<ResponseEntity<String>> getUsername(Principal principal) {
        if(principal == null){
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(principal.getName(), HttpStatus.OK));
    }
}
