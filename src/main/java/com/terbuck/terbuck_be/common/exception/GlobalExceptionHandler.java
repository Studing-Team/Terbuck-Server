package com.terbuck.terbuck_be.common.exception;

import com.terbuck.terbuck_be.common.dto.ErrorStatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorStatusResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorStatusResponse.of(e.getErrorCode().getStatus(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorStatusResponse> handleEnumTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String paramName = e.getName();

        String message = String.format("파라미터명 : %s에 올바르지 않은 값이 전달되었습니다.", paramName);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorStatusResponse.of(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorStatusResponse> handleMissingRequestParamException(MissingServletRequestParameterException e) {
        String paramType = e.getParameterType();
        String paramName = e.getParameterName();

        String message = String.format("타입:'%s', 파라미터명:'%s'에 해당하는 파라미터 정보는 필수입니다.", paramType, paramName);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorStatusResponse.of(HttpStatus.BAD_REQUEST.value(), message));
    }
}
