package org.plutotramble.category;

import org.modelmapper.ModelMapper;
import org.plutotramble.authentication.UserAccountRepository;
import org.plutotramble.shared.exceptions.InvalidItemPropertyException;
import org.plutotramble.shared.exceptions.ItemNotFoundException;
import org.plutotramble.shared.entities.CategoryEntity;
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

    public CategoryService(UserAccountRepository userRepository,
                           CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Async
    public CompletableFuture<List<CategoryDTO>> categoriesByUserName(String username) throws ExecutionException, InterruptedException {
        UUID userId = getUserUUIDByName(username).get();

        List<CategoryEntity> categories = categoryRepository.getCategoryEntitiesByUserAccount_IdOrderByName(userId).get();

        List<CategoryDTO> categoriesDTO = categories.stream().map(category -> {
            CategoryDTO dto = new CategoryDTO();
            dto.name = category.getName();
            dto.color = category.getColor();
            dto.id = category.getId();
            dto.dateCreated = category.getDateCreated().toLocalDateTime();
            return dto;
        }).toList();

        return CompletableFuture.completedFuture(categoriesDTO);
    }

    @Async
    public CompletableFuture<CategoryDTO> createCategory(String username, CategoryDTO categoryDTO) throws ExecutionException, InterruptedException, InvalidItemPropertyException {
        // Verification
        if(categoryDTO.color.length() > 7){
            throw new InvalidItemPropertyException("Category color is too long. Maximum length is 7.");
        }
        if(categoryDTO.name.length() > 30) {
            throw new InvalidItemPropertyException("Category name is too long. Maximum length is 30.");
        }
        if(categoryDTO.name.length() < 2) {
            throw new InvalidItemPropertyException("Category name must be at least 2 characters.");
        }

        // Create entity
        CategoryEntity category = new CategoryEntity();
        category.setName(categoryDTO.name);
        category.setColor(categoryDTO.color);
        category.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        category.setUserAccount(userRepository.findByUsername(username).get());

        categoryRepository.save(category);

        categoryDTO.id = category.getId();
        categoryDTO.dateCreated = category.getDateCreated().toLocalDateTime();

        return CompletableFuture.completedFuture(categoryDTO);
    }

    @Async
    public void deleteCategory(String username, UUID categoryId) throws ExecutionException, InterruptedException, ItemNotFoundException {
        UUID userId = getUserUUIDByName(username).get();

        CategoryEntity category = categoryRepository.getCategoryEntityByIdAndUserAccount_Id(categoryId, userId).get();

        if(category == null){
            throw new ItemNotFoundException("Category not found");
        }

        categoryRepository.deleteById(categoryId);
    }

    @Async
    protected CompletableFuture<UUID> getUserUUIDByName(String name) throws ExecutionException, InterruptedException {
        return CompletableFuture.completedFuture(userRepository.findByUsername(name).get().getId());
    }
}
