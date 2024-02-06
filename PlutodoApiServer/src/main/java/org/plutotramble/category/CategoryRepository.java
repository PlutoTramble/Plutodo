package org.plutotramble.category;

import org.plutotramble.shared.entities.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Object> {

    @Async
    Future<List<CategoryEntity>> getCategoryEntitiesByUserAccount_IdOrderByName(UUID userAccountId);

    @Async
    Future<CategoryEntity> getCategoryEntityByNameContaining(String name);

    @Async
    Future<CategoryEntity> getCategoryEntityByIdAndUserAccount_Id(UUID id, UUID userAccountId);
}
