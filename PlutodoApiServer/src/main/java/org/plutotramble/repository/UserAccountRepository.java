package org.plutotramble.repository;

import org.plutotramble.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;


import java.util.concurrent.Future;

public interface UserAccountRepository extends CrudRepository<UserAccountEntity, Object> {
    @Async
    Future<UserAccountEntity> findByUsername(String Username);

    @Async
    Future<Boolean> existsByUsernameIgnoreCase(String username);

    @Async
    Future<Boolean> existsByEmailAddressIgnoreCase(String email);

    @Async
    @Procedure(procedureName = "create_user")
    void createUser(
            @Param("p_email") String email,
            @Param("p_username") String username,
            @Param("p_password") String password);
}
