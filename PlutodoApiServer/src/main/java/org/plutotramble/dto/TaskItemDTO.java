package org.plutotramble.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskItemDTO extends SmallTaskItemDTO {
    public String description;
    public LocalDateTime dateCreated;
    public UUID categoryId;
}
