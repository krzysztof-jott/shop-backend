package com.ex.shop.security.exception;

import com.ex.shop.common.exception.BusinessException;

public class RegisterException extends BusinessException {

    public RegisterException(String message) {
        super(message);
    }
}