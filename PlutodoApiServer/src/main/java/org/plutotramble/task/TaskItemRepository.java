package org.plutotramble.task;

import org.plutotramble.shared.entities.TaskItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface TaskItemRepository extends CrudRepository<TaskItemEntity, Object> {

    @Async
    CompletableFuture<List<TaskItemEntity>> getTaskItemEntitiesByCategory_IdAndCategory_UserAccount_Id(UUID category_id, UUID userAccount_id);

    @Async
    CompletableFuture<TaskItemEntity> getTaskItemEntityByIdAndCategory_UserAccount_Id(UUID id, UUID userAccount_id);

    @Async
    CompletableFuture<List<TaskItemEntity>> getTaskItemEntitiesByCategory_UserAccount_Id(UUID userAccount_id);
}
