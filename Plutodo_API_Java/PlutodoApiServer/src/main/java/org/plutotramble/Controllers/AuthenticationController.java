package org.plutotramble.Controllers;

import org.plutotramble.Entities.DTOs.LoginDTO;
import org.plutotramble.Entities.UserAccountEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/Authentication")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    AuthenticationController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Async
    @PostMapping(value = "/login")
    public CompletableFuture<ResponseEntity<Void>> login(@RequestBody LoginDTO loginDTO){
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(loginDTO.username, loginDTO.password);
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }
}
