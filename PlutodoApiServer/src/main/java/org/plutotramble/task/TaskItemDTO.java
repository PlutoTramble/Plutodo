package org.plutotramble.task;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskItemDTO extends SmallTaskItemDTO {
    public String description;
    public LocalDateTime dateCreated;
    public UUID categoryId;
}
