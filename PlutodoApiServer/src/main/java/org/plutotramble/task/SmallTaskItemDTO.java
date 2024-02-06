package org.plutotramble.task;

import java.time.LocalDateTime;
import java.util.UUID;

public class SmallTaskItemDTO {
    public UUID id;
    public String name;
    public boolean isFinished;
    public LocalDateTime dateDue;
}
