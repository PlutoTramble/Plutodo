package org.plutotramble.Repository;

import org.plutotramble.Entities.UserAccountEntity;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;


import java.util.concurrent.Future;

public interface UserAccountRepository extends CrudRepository<UserAccountEntity, Object> {
    @Async
    Future<UserAccountEntity> findByUsername(String Username);

    @Procedure(procedureName = "create_user")
    boolean createUser(
            @Param("p_email") String email,
            @Param("p_username") String username,
            @Param("p_password") String password);
}