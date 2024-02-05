package org.plutotramble.category;


import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.plutotramble.authentication.UserAccountRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CategoryService {
    private final UserAccountRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public CategoryService(UserAccountRepository userRepository,
                           CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Async
    public CompletableFuture<List<CategoryDTO>> categoriesByUserName(String username) throws ExecutionException, InterruptedException {
        UUID userId = getUserUUIDByName(username).get();

        List<CategoryEntity> categories = categoryRepository.getCategoryEntitiesByUserAccountIdOrderByName(userId).get();
        List<CategoryDTO> categoriesDTO = modelMapper.map(categories, new TypeToken<List<CategoryEntity>>() {}.getType());

        return CompletableFuture.completedFuture(categoriesDTO);
    }

    @Async
    public CompletableFuture<CategoryDTO> createCategory(String username, CategoryDTO categoryDTO) throws ExecutionException, InterruptedException {
        // Verification
        if(categoryDTO.color.length() > 7){
            throw new RuntimeException("Category color is too long. Maximum length is 7.");
        }
        if(categoryDTO.name.length() > 30) {
            throw new RuntimeException("Category name is too long. Maximum length is 30.");
        }
        if(categoryDTO.name.length() < 2) {
            throw new RuntimeException("Category name must be at least 2 characters.");
        }

        // Create entity
        UUID userId = getUserUUIDByName(username).get();

        CategoryEntity category = new CategoryEntity();
        category.setName(categoryDTO.name);
        category.setColor(categoryDTO.color);
        category.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        category.setUserAccountId(userId);

        categoryRepository.save(category);

        categoryDTO.id = category.getId();
        categoryDTO.dateCreated = category.getDateCreated().toLocalDateTime();

        return CompletableFuture.completedFuture(categoryDTO);
    }

    @Async
    protected CompletableFuture<UUID> getUserUUIDByName(String name) throws ExecutionException, InterruptedException {
        return CompletableFuture.completedFuture(userRepository.findByUsername(name).get().getId());
    }
}
