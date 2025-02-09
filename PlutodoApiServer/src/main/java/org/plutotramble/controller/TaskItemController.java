package org.plutotramble.controller;

import org.plutotramble.dto.SmallTaskItemDTO;
import org.plutotramble.dto.TaskItemDTO;
import org.plutotramble.exception.InvalidItemPropertyException;
import org.plutotramble.exception.ItemNotFoundException;
import org.plutotramble.service.TaskItemService;
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
@RequestMapping(value = "/api/Task")
public class TaskItemController {

    private final TaskItemService taskItemService;

    public TaskItemController(TaskItemService taskItemService){
        this.taskItemService = taskItemService;
    }

    @Async
    @GetMapping(value = "/getTasksFromCategory")
    public CompletableFuture<ResponseEntity<List<TaskItemDTO>>> getTasksFromCategoru(@RequestParam UUID id, Principal principal) throws ExecutionException, InterruptedException {
        List<TaskItemDTO> tasks = taskItemService.taskItemsByCategory(id, principal.getName()).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(tasks, HttpStatus.OK));
    }

    @Async
    @GetMapping(value = "/getAll")
    public CompletableFuture<ResponseEntity<List<TaskItemDTO>>> getAll(Principal principal) throws ExecutionException, InterruptedException {
        List<TaskItemDTO> tasks = taskItemService.allTasks(principal.getName()).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(tasks, HttpStatus.OK));
    }

    @Async
    @GetMapping(value = "/getSmallTasksFromCategory")
    public CompletableFuture<ResponseEntity<List<SmallTaskItemDTO>>> getSmallTasksFromCategoru(@RequestParam UUID categoryId, Principal principal) throws ExecutionException, InterruptedException {
        List<SmallTaskItemDTO> tasks = taskItemService.smallTaskItemsByCategory(categoryId, principal.getName()).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(tasks, HttpStatus.OK));
    }

    @Async
    @GetMapping(value = "/getTaskDetail")
    public CompletableFuture<ResponseEntity<TaskItemDTO>> getTaskDetail(@RequestParam UUID id, Principal principal) throws ExecutionException, InterruptedException, ItemNotFoundException {
        TaskItemDTO taskItemDTO = taskItemService.taskItemDetail(id, principal.getName()).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(taskItemDTO, HttpStatus.OK));
    }

    @Async
    @PostMapping(value = "/add")
    public CompletableFuture<ResponseEntity<TaskItemDTO>> add(@RequestBody TaskItemDTO taskItemDTO, Principal principal) throws InvalidItemPropertyException, ExecutionException, InterruptedException, ItemNotFoundException {
        taskItemDTO = taskItemService.createNewTask(taskItemDTO, principal.getName()).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(taskItemDTO, HttpStatus.OK));
    }

    @Async
    @PutMapping(value = "/edit")
    public CompletableFuture<ResponseEntity<TaskItemDTO>> edit(@RequestBody TaskItemDTO taskItemDTO, Principal principal) throws InvalidItemPropertyException, ExecutionException, InterruptedException, ItemNotFoundException {
        taskItemDTO = taskItemService.editTask(taskItemDTO, principal.getName()).get();
        return CompletableFuture.completedFuture(new ResponseEntity<>(taskItemDTO, HttpStatus.OK));
    }

    @Async
    @DeleteMapping(value = "/delete")
    public CompletableFuture<ResponseEntity<Object>> delete(@RequestParam UUID id, Principal principal) throws ExecutionException, InterruptedException, ItemNotFoundException {
        taskItemService.deleteTask(id, principal.getName());
        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }
}
