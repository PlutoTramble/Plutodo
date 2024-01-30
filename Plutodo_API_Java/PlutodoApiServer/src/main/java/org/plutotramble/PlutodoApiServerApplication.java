package org.plutotramble;

import org.plutotramble.Entities.UserAccountEntity;
import org.plutotramble.Repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class PlutodoApiServerApplication {

    private static final Logger log = LoggerFactory.getLogger(PlutodoApiServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PlutodoApiServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner createNewUser(UserAccountRepository repository){
        return (args -> {
            UserAccountEntity userAccount = repository.findByUsername("admin").get();
            if (userAccount == null) {
                log.info("admin does not exist, creating one");
                createAdminUser(repository);
            } else {
                log.info("admin exists");
            }
        });
    }

    protected void createAdminUser(UserAccountRepository repository){
        repository.createUser("test@test.test", "admin", "12345");
        log.info("User admin is created.");
    }
}
