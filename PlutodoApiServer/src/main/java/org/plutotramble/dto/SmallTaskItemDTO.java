package org.plutotramble.dto;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public class SmallTaskItemDTO {
    public UUID id;
    public String name;
    public boolean isFinished;

    @Nullable
    public LocalDateTime dateDue;
}
