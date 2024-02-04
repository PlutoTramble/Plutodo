package org.plutotramble.authentication;

import org.plutotramble.authentication.exceptions.EmailAddressAlreadyExistsAuthenticationException;
import org.plutotramble.authentication.exceptions.InvalidEmailAddressException;
import org.plutotramble.authentication.exceptions.InvalidPasswordException;
import org.plutotramble.authentication.exceptions.InvalidUsernameException;
import org.plutotramble.authentication.viewmodels.RegisterViewmodel;
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
    public void CreateUser(RegisterViewmodel registerViewmodel) throws ExecutionException, InterruptedException {
        logger.atTrace().log("Checking if new user (" + registerViewmodel.username + ") can be created.");

        if(userRepository.existsByUsernameIgnoreCase(registerViewmodel.username).get()){
            throw new InvalidUsernameException(
                    "The username is already used by another user."
            );
        }

        if (userRepository.existsByEmailAddressIgnoreCase(registerViewmodel.emailAddress).get()) {
            throw new EmailAddressAlreadyExistsAuthenticationException(
                    "The email address is already used by another user."
            );
        }

        if(registerViewmodel.username.isEmpty()){
            throw new InvalidUsernameException("The username is invalid.");
        }

        if(!isValidEmailAddress(registerViewmodel.emailAddress)){
            throw new InvalidEmailAddressException("The email address is invalid.");
        }

        if(!registerViewmodel.password.equals(registerViewmodel.passwordConfirm)){
            throw new InvalidPasswordException("Password and password confirmation are not the same");
        }

        if(!isPasswordSecure(registerViewmodel.password)){
            throw new InvalidPasswordException(
                    "The password needs to contains at least " +
                    "one lower and upper case letter, one number, " +
                    "and one special character."
            );
        }

        logger.atTrace().log(registerViewmodel.username + "can be created.");

        userRepository.createUser(registerViewmodel.emailAddress, registerViewmodel.username, registerViewmodel.password);

        logger.atTrace().log(registerViewmodel.username + "has been created");
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
