package org.plutotramble.task;

import org.plutotramble.authentication.UserAccountRepository;
import org.plutotramble.category.CategoryRepository;
import org.plutotramble.shared.entities.TaskItemEntity;
import org.plutotramble.shared.exceptions.InvalidItemPropertyException;
import org.plutotramble.shared.exceptions.ItemNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class TaskItemService {

    private final TaskItemRepository taskItemRepository;

    private final UserAccountRepository userRepository;

    private final CategoryRepository categoryRepository;

    public TaskItemService(TaskItemRepository taskItemRepository, UserAccountRepository userRepository, CategoryRepository categoryRepository) {
        this.taskItemRepository = taskItemRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Async
    public CompletableFuture<List<TaskItemDTO>> taskItemsByCategory(UUID categoryId, String username) throws ExecutionException, InterruptedException {
        UUID userId = getUserUUIDByName(username).get();

        List<TaskItemEntity> tasks =
                taskItemRepository.getTaskItemEntitiesByCategory_IdAndCategory_UserAccount_Id(categoryId, userId).get();

        List<TaskItemDTO> taskItemDTOS = tasks.stream().map(this::convertFromEntity).toList();

        return CompletableFuture.completedFuture(taskItemDTOS);
    }

    @Async
    public CompletableFuture<List<SmallTaskItemDTO>> smallTaskItemsByCategory(UUID categoryId, String username) throws ExecutionException, InterruptedException {
        UUID userId = getUserUUIDByName(username).get();

        List<TaskItemEntity> tasks =
                taskItemRepository.getTaskItemEntitiesByCategory_IdAndCategory_UserAccount_Id(categoryId, userId).get();

        List<SmallTaskItemDTO> taskItemDTOS = tasks.stream().map(task -> {
            SmallTaskItemDTO dto = new SmallTaskItemDTO();
            dto.id = task.getId();
            dto.name = task.getName();
            dto.dateDue = task.getDateDue().toLocalDateTime();
            dto.isFinished = task.getFinished();
            return dto;
        }).toList();

        return CompletableFuture.completedFuture(taskItemDTOS);
    }

    @Async
    public CompletableFuture<TaskItemDTO> taskItemDetail(UUID id, String username) throws ExecutionException, InterruptedException, ItemNotFoundException {
        UUID userId = getUserUUIDByName(username).get();

        TaskItemEntity task = taskItemRepository.getTaskItemEntityByIdAndCategory_UserAccount_Id(id, userId).get();

        if(task == null){
            throw new ItemNotFoundException("Requested task doesn't exist");
        }

        return CompletableFuture.completedFuture(convertFromEntity(task));
    }

    @Async
    public CompletableFuture<List<TaskItemDTO>> allTasks(String username) throws ExecutionException, InterruptedException {
        UUID userId = getUserUUIDByName(username).get();

        List<TaskItemEntity> tasks = taskItemRepository.getTaskItemEntitiesByCategory_UserAccount_Id(userId).get();

        List<TaskItemDTO> taskItemDTOS = tasks.stream().map(this::convertFromEntity).toList();

        return CompletableFuture.completedFuture(taskItemDTOS);
    }

    @Async
    public CompletableFuture<TaskItemDTO> createNewTask(TaskItemDTO taskReceived, String username) throws InvalidItemPropertyException, ExecutionException, InterruptedException, ItemNotFoundException {
        UUID userId = getUserUUIDByName(username).get();

        // Verification
        verifyTaskProperties(taskReceived, userId);

        // Create entity
        TaskItemEntity task = new TaskItemEntity();
        task.setName(taskReceived.name);
        task.setDescription(taskReceived.description);
        task.setFinished(taskReceived.isFinished);
        task.setDateCreated(Timestamp.valueOf(LocalDateTime.now()));
        task.setDateDue(Timestamp.valueOf(taskReceived.dateDue));
        task.setCategory(categoryRepository.findById(taskReceived.categoryId).get());

        taskItemRepository.save(task);

        taskReceived.id = task.getId();
        taskReceived.dateCreated = task.getDateCreated().toLocalDateTime();

        return CompletableFuture.completedFuture(taskReceived);
    }

    @Async
    public CompletableFuture<TaskItemDTO> editTask(TaskItemDTO taskItemDTO, String username) throws ExecutionException, InterruptedException, InvalidItemPropertyException, ItemNotFoundException {
        UUID userId = getUserUUIDByName(username).get();

        // Verification
        verifyTaskProperties(taskItemDTO, userId);

        // Edit entity
        TaskItemEntity task =
                taskItemRepository.getTaskItemEntityByIdAndCategory_UserAccount_Id(taskItemDTO.id, userId).get();

        if(task == null) {
            throw new ItemNotFoundException("TaskItem not found");
        }

        task.setName(taskItemDTO.name);
        task.setDateDue(Timestamp.valueOf(taskItemDTO.dateDue));
        task.setFinished(taskItemDTO.isFinished);
        task.setDescription(taskItemDTO.description);

        taskItemRepository.save(task);

        taskItemDTO.dateCreated = task.getDateCreated().toLocalDateTime();

        return CompletableFuture.completedFuture(taskItemDTO);
    }

    @Async
    public void deleteTask(UUID id, String username) throws ExecutionException, InterruptedException, ItemNotFoundException {
        UUID userId = getUserUUIDByName(username).get();

        TaskItemEntity task =
                taskItemRepository.getTaskItemEntityByIdAndCategory_UserAccount_Id(id, userId).get();

        if(task == null) {
            throw new ItemNotFoundException("Task not found");
        }

        taskItemRepository.delete(task);
    }

    private void verifyTaskProperties(TaskItemDTO taskItemDTO, UUID userId) throws InvalidItemPropertyException, InterruptedException, ExecutionException, ItemNotFoundException {
        if(taskItemDTO.name.length() > 30) {
            throw new InvalidItemPropertyException("Task's name is too long. Maximum length is 30");
        }
        if(taskItemDTO.categoryId == null) {
            throw new InvalidItemPropertyException("Task needs to be affiliated to a category");
        }
        if(categoryRepository.existsCategoryEntityByUserAccount_Id(userId).get()) {
            throw new ItemNotFoundException("Category doesn't exist.");
        }
        if(taskItemDTO.description.length() > 8192) {
            throw new InvalidItemPropertyException("Task's description is too long. Maximum length is 8192");
        }
    }

    private TaskItemDTO convertFromEntity(TaskItemEntity task){
        TaskItemDTO dto = new TaskItemDTO();
        dto.id = task.getId();
        dto.name = task.getName();
        dto.dateCreated = task.getDateCreated().toLocalDateTime();
        dto.dateDue = task.getDateDue().toLocalDateTime();
        dto.description = task.getDescription();
        dto.isFinished = task.getFinished();
        dto.categoryId = task.getCategory().getId();

        return dto;
    }

    @Async
    protected CompletableFuture<UUID> getUserUUIDByName(String name) throws ExecutionException, InterruptedException {
        return CompletableFuture.completedFuture(userRepository.findByUsername(name).get().getId());
    }
}
