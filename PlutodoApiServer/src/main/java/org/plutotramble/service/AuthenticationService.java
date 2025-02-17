package org.plutotramble.service;

import org.plutotramble.client.Nextcloud.NextcloudClient;
import org.plutotramble.dto.NextCloudLoginDTO;
import org.plutotramble.entity.NextcloudUserInfoEntity;
import org.plutotramble.exception.authentication.*;
import org.plutotramble.repository.NextcloudUserInfoRepository;
import org.plutotramble.repository.UserAccountRepository;
import org.plutotramble.dto.RegisterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class AuthenticationService{
    private final UserAccountRepository userRepository;
    private final NextcloudUserInfoRepository nextcloudUserRepository;

    private final NextcloudClient nextcloudClient;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserAccountRepository userRepository, NextcloudUserInfoRepository nextcloudUserRepository, NextcloudClient nextcloudClient) {
        this.userRepository = userRepository;
        this.nextcloudUserRepository = nextcloudUserRepository;
        this.nextcloudClient = nextcloudClient;
    }

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

    @Async
    public void addNextcloudUserAccount(NextCloudLoginDTO nextCloudLogin, String username) throws ExecutionException, InterruptedException {
        UUID userId = getUserUUIDByName(username).get();

        if(nextcloudUserRepository.existsByUserAccount_Id(userId).get()) {
            nextcloudUserRepository.deleteByUserAccount_Id(userId);
        }

        // Checks if user exists, then creates it
        if(nextcloudClient.pingUserAccount(nextCloudLogin).get()) {
            nextcloudUserRepository.createNextcloudLogin(nextCloudLogin.username, nextCloudLogin.password, nextCloudLogin.serverUrl);
        }
        else {
            throw new NextcloudException("Could not login to nextcloud.");
        }
    }

    @Async
    public void removeNextcloudUserAccount(String userAccountName) throws ExecutionException, InterruptedException {
        UUID userId = getUserUUIDByName(userAccountName).get();

        if(nextcloudUserRepository.existsByUserAccount_Id(userId).get()) {
            nextcloudUserRepository.deleteByUserAccount_Id(userId);
        }
        else {
            throw new NextcloudException("Nextcloud user account does not exist.");
        }
    }

    @Async
    public Future<Boolean> checkNextcloudUserAccount(String userAccountName) throws ExecutionException, InterruptedException {
        NextCloudLoginDTO nextCloudLogin = getNextCloudLoginInfoByUsername(userAccountName).get();

        return CompletableFuture.completedFuture(nextcloudClient.pingUserAccount(nextCloudLogin).get());
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

    @Async
    protected Future<NextCloudLoginDTO> getNextCloudLoginInfoByUsername(String username) throws ExecutionException, InterruptedException {
        UUID userId = getUserUUIDByName(username).get();
        NextcloudUserInfoEntity nextcloudUserInfo = nextcloudUserRepository.getNextcloudUserInfoEntityByUserAccount_Id(userId).get();

        // Fingers crossed that should be able to decrypt the stores password. Might be a dirty way to do it.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        NextCloudLoginDTO loginDTO = new NextCloudLoginDTO();
        loginDTO.password = encoder.encode(nextcloudUserInfo.getPassword());
        loginDTO.username = nextcloudUserInfo.getUsername();

        return CompletableFuture.completedFuture(loginDTO);
    }

    @Async
    protected CompletableFuture<UUID> getUserUUIDByName(String name) throws ExecutionException, InterruptedException {
        return CompletableFuture.completedFuture(userRepository.findByUsername(name).get().getId());
    }
}
