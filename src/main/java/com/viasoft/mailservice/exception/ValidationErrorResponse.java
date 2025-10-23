package com.viasoft.mailservice.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {

    private Map<String, String> fieldErrors;

    public ValidationErrorResponse(int status, String message, String path, Map<String, String> fieldErrors) {
        super(status, message, path);
        this.fieldErrors = fieldErrors;
    }
}