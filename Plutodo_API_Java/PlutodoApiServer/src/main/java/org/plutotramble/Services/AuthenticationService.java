package org.plutotramble.Services;

import org.plutotramble.Entities.DTOs.RegisterDTO;
import org.plutotramble.Exceptions.*;
import org.plutotramble.Repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class AuthenticationService{
    @Autowired
    private UserAccountRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);



    @Async
    public void CreateUser(RegisterDTO registerDTO) throws ExecutionException, InterruptedException {
        logger.atTrace().log("Checking if new user (" + registerDTO.username + ") can be created.");

        if(userRepository.existsByUsernameIgnoreCase(registerDTO.username).get()){
            throw new InvalidUsernameException(
                    "The username is already used by another user."
            );
        }

        if (userRepository.existsByEmailAddressIgnoreCase(registerDTO.emailAddress).get()) {
            throw new EmailAddressAlreadyExistsAuthenticationException(
                    "The email address is already used by another user."
            );
        }

        if(registerDTO.username.isEmpty()){
            throw new InvalidUsernameException("The username is invalid.");
        }

        if(!isValidEmailAddress(registerDTO.emailAddress)){
            throw new InvalidEmailAddressException("The email address is invalid.");
        }

        if(!registerDTO.password.equals(registerDTO.passwordConfirm)){
            throw new InvalidPasswordException("Password and password confirmation are not the same");
        }

        if(!isPasswordSecure(registerDTO.password)){
            throw new InvalidPasswordException(
                    "The password needs to contains at least " +
                    "one lower and upper case letter, one number, " +
                    "and one special character."
            );
        }

        logger.atTrace().log(registerDTO.username + "can be created.");

        userRepository.createUser(registerDTO.emailAddress, registerDTO.username, registerDTO.password);

        logger.atTrace().log(registerDTO.username + "has been created");
    }

    /**
     * This method assures that the password is eight characters minimum,
     * at least one upper and lower case letter, one number and one special character.
     * @param password
     * @return
     */
    private boolean isPasswordSecure(String password){
        return password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$");
    }

    private boolean isValidEmailAddress(String emailAddress){
        return emailAddress.matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$");
    }
}
