package com.crocobet.authservice.exception;

import com.crocobet.authservice.exception.detail.HandledExceptionDetail;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HandledException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final HandledExceptionDetail detail;

    public HandledException(HttpStatus status, HandledExceptionDetail detail) {
        super(detail.getMessage());
        this.httpStatus = status;
        this.detail = detail;
    }

    public HandledException(HttpStatus status, String exception) {
        super(exception);
        this.httpStatus = status;
        this.detail = new HandledExceptionDetail(exception);
    }

    public HandledException(String exception) {
        super(exception);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.detail = new HandledExceptionDetail(exception);
    }

}
