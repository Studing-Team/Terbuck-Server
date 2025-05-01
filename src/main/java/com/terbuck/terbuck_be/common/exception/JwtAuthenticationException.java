package com.terbuck.terbuck_be.common.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
  public JwtAuthenticationException(String msg) {
    super(msg);
  }
}

