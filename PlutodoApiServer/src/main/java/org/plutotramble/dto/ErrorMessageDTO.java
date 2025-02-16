package org.plutotramble.dto;

public class ErrorMessageDTO {
    public String error = "";

    public ErrorMessageDTO(String error) {
        this.error = error.trim();
    }

}
