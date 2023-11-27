package com.crocobet.authservice.exception.advice;

import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.exception.detail.HandledExceptionDetail;
import com.crocobet.authservice.exception.detail.ValidationDetail;
import com.crocobet.authservice.exception.model.FieldValidationError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@ControllerAdvice
public class ControllerAdvicer {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HandledExceptionDetail> defaultHandler(Exception exception) {
        log.error(exception.toString());
        return new ResponseEntity<>(new HandledExceptionDetail("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ValidationDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation exception encountered: {}", ex.toString());
        return new ValidationDetail(List.of(new FieldValidationError("data_integrity", "The provided data violates integrity constraints")));
    }

    @ExceptionHandler(HandledException.class)
    public ResponseEntity<HandledExceptionDetail> handleHandledException(HandledException exception) {
        log.error(exception.toString());
        return new ResponseEntity<>(exception.getDetail(), exception.getHttpStatus());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ValidationDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Constraint violation exception encountered: {}", ex.toString());
        return new ValidationDetail(buildValidationErrors(ex.getBindingResult()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ValidationDetail handleConstraintViolation(ConstraintViolationException ex) {
        log.error("Constraint violation exception encountered: {}", ex.toString());
        return new ValidationDetail(buildValidationErrors(ex.getConstraintViolations()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseBody
    public ValidationDetail handleConstraintViolation(MissingServletRequestPartException ex) {
        log.error("Servlet request part exception encountered: {}", ex.toString());
        return new ValidationDetail(List.of(new FieldValidationError(ex.getRequestPartName(), ex.getMessage())));
    }

    private List<FieldValidationError> buildValidationErrors(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .map((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return new FieldValidationError(fieldName, errorMessage);
                })
                .collect(Collectors.toList());
    }

    private List<FieldValidationError> buildValidationErrors(Set<ConstraintViolation<?>> violations) {
        return violations
                .stream()
                .map(violation -> {
                    String field = Objects.requireNonNull(StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                                    .reduce((first, second) -> second)
                                    .orElse(null))
                            .toString();

                    return new FieldValidationError(field, violation.getMessage());
                }).
                collect(Collectors.toList());
    }

}
