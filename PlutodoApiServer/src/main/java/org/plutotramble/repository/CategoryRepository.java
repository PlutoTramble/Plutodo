package org.plutotramble.repository;

import org.plutotramble.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Object> {

    @Async
    Future<List<CategoryEntity>> getCategoryEntitiesByUserAccount_IdOrderByName(UUID userAccountId);

    @Async
    Future<CategoryEntity> getCategoryEntityByNameContaining(String name);

    @Async
    Future<CategoryEntity> getCategoryEntityByIdAndUserAccount_Id(UUID id, UUID userAccountId);

    @Async
    CompletableFuture<Boolean> existsCategoryEntityByUserAccount_Id(UUID userAccount_id);
}
