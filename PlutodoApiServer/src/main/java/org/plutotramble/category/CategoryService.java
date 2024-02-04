package org.plutotramble.category;


import org.modelmapper.ModelMapper;
import org.plutotramble.authentication.UserAccountRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
        UUID userId = userRepository.findByUsername(username).get().getId();

        List<CategoryEntity> categories = categoryRepository.getCategoryEntitiesByUserAccountIdOrderByName(userId).get();
        ModelMapper modelMapper = new ModelMapper();

        List<CategoryDTO> categoriesDTO =
                categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        return CompletableFuture.completedFuture(categoriesDTO);
    }
}
