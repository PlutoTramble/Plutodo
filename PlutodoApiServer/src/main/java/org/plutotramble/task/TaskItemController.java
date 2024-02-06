package org.plutotramble.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/api/Task")
public class TaskItemController {

    private final TaskItemService taskItemService;

    public TaskItemController(TaskItemService taskItemService){
        this.taskItemService = taskItemService;
    }

    @Async
    @GetMapping(value = "/getTasksFromCategory/")
    public CompletableFuture<ResponseEntity<List<TaskItemDTO>>> getTasksFromCategoru(@RequestParam UUID categoryId, Principal principal) throws ExecutionException, InterruptedException {
        List<TaskItemDTO> tasks = taskItemService.taskItemsByCategory(categoryId, principal.getName()).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(tasks, HttpStatus.OK));
    }

    @Async
    @GetMapping(value = "/getSmallTasksFromCategory/")
    public CompletableFuture<ResponseEntity<List<SmallTaskItemDTO>>> getSmallTassFromCategoru(@RequestParam UUID categoryId, Principal principal) throws ExecutionException, InterruptedException {
        List<SmallTaskItemDTO> tasks = taskItemService.smallTaskItemsByCategory(categoryId, principal.getName()).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(tasks, HttpStatus.OK));
    }
}
