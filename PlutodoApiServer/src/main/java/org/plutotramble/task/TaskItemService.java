package org.plutotramble.task;

import org.plutotramble.authentication.UserAccountRepository;
import org.plutotramble.shared.entities.TaskItemEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class TaskItemService {

    private final TaskItemRepository taskItemRepository;

    private final UserAccountRepository userRepository;

    public TaskItemService(TaskItemRepository taskItemRepository, UserAccountRepository userRepository) {
        this.taskItemRepository = taskItemRepository;
        this.userRepository = userRepository;
    }

    @Async
    public CompletableFuture<List<TaskItemDTO>> taskItemsByCategory(UUID categoryId, String username) throws ExecutionException, InterruptedException {
        UUID userId = getUserUUIDByName(username).get();

        List<TaskItemEntity> tasks =
                taskItemRepository.getTaskItemEntitiesByCategory_IdAndCategory_UserAccount_Id(categoryId, userId).get();

        List<TaskItemDTO> taskItemDTOS = tasks.stream().map(task -> {
            TaskItemDTO dto = new TaskItemDTO();
            dto.id = task.getId();
            dto.name = task.getName();
            dto.dateCreated = task.getDateCreated().toLocalDateTime();
            dto.dateDue = task.getDateDue().toLocalDateTime();
            dto.description = task.getDescription();
            dto.isFinished = task.getFinished();
            dto.categoryId = task.getCategory().getId();
            return dto;
        }).toList();

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
    protected CompletableFuture<UUID> getUserUUIDByName(String name) throws ExecutionException, InterruptedException {
        return CompletableFuture.completedFuture(userRepository.findByUsername(name).get().getId());
    }
}
