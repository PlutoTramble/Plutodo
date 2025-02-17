package org.plutotramble.repository;

import org.plutotramble.entity.NextcloudUserInfoEntity;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.UUID;
import java.util.concurrent.Future;

public interface NextcloudUserInfoRepository extends CrudRepository<NextcloudUserInfoEntity, Object> {

    @Async
    Future<NextcloudUserInfoEntity> getNextcloudUserInfoEntityByUserAccount_Id(UUID userAccountId);
    
    @Async
    Future<Boolean> existsByUserAccount_Id(UUID userAccountId);

    @Async
    void deleteByUserAccount_Id(UUID userAccountId);

    @Async
    @Procedure(procedureName = "create_nextcloud_login")
    void createNextcloudLogin(
            @Param("p_username") String username,
            @Param("p_password") String password,
            @Param("p_server") String serverUrl
    );
}
