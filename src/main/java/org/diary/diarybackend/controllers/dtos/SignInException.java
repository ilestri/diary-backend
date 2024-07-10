package org.diary.diarybackend.controllers.dtos;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class SignInException extends RuntimeException {
    @Getter
    private final HttpStatus status;
    private final String message;

    public SignInException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
