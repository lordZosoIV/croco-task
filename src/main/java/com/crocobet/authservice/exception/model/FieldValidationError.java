package com.crocobet.authservice.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class FieldValidationError {
    private String name;
    private String error;
}
