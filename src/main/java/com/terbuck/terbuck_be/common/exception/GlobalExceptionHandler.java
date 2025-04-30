package com.terbuck.terbuck_be.common.exception;

import com.terbuck.terbuck_be.common.dto.ErrorStatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorStatusResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorStatusResponse.of(e.getErrorCode().getStatus(), e.getErrorCode().getMessage()));
    }
}
