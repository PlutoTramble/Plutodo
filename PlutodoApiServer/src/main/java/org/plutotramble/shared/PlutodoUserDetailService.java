package org.plutotramble.shared;

import org.plutotramble.shared.entities.UserAccountEntity;
import org.plutotramble.authentication.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class PlutodoUserDetailService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccountEntity userAccount;
        try {
            userAccount = userRepository.findByUsername(username).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (userAccount == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return User.builder()
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .roles("USER")
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .build();
    }
}
