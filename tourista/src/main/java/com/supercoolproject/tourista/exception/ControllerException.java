package com.supercoolproject.tourista.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ControllerException extends RuntimeException {
    private HttpStatus httpStatus;

    public ControllerException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public String errorMessage() {
        return httpStatus.value() + ":" + getMessage();
    }
}
