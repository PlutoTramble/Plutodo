package org.plutotramble.category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/Category")
public class CategoryController {

    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @Async
    @GetMapping(value = "/get")
    public CompletableFuture<ResponseEntity<Object>> get(Principal principal) throws ExecutionException, InterruptedException {

        List<CategoryDTO> categories = categoryService.categoriesByUserName(principal.getName()).get();

        return CompletableFuture.completedFuture(new ResponseEntity<>(categories, HttpStatus.OK));
    }
}
