package com.terbuck.terbuck_be.common.exception;

import lombok.Getter;

// 공통 부모 클래스
@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode ;
  }
}
