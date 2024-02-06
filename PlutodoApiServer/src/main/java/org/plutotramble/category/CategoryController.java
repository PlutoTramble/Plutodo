package org.plutotramble.category;

import org.plutotramble.shared.exceptions.InvalidItemPropertyException;
import org.plutotramble.shared.exceptions.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/api/Category")
public class CategoryController {

    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @Async
    @GetMapping(value = "/getFromUser")
    public CompletableFuture<ResponseEntity<List<CategoryDTO>>> getFromUser(Principal principal) throws ExecutionException, InterruptedException {
        List<CategoryDTO> categories = categoryService.categoriesByUserName(principal.getName()).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(categories, HttpStatus.OK));
    }

    @Async
    @PostMapping(value = "/add")
    public CompletableFuture<ResponseEntity<Object>> add(@RequestBody CategoryDTO categoryDTO, Principal principal) throws ExecutionException, InterruptedException, InvalidItemPropertyException {
        categoryDTO = categoryService.createCategory(principal.getName(), categoryDTO).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(categoryDTO, HttpStatus.OK));
    }

    @Async
    @PutMapping(value = "/edit")
    public CompletableFuture<ResponseEntity<CategoryDTO>> edit(@RequestBody CategoryDTO categoryDTO, Principal principal) throws InvalidItemPropertyException, ExecutionException, InterruptedException, ItemNotFoundException {
        categoryDTO = categoryService.editCategory(categoryDTO, principal.getName()).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(categoryDTO, HttpStatus.OK));
    }

    @Async
    @DeleteMapping(value = "/delete")
    public CompletableFuture<ResponseEntity<Object>> delete(@RequestParam UUID id, Principal principal) throws ExecutionException, InterruptedException, ItemNotFoundException {
        categoryService.deleteCategory(principal.getName(), id);
        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }
}
