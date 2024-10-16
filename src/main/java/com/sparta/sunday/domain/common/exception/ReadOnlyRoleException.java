package com.sparta.sunday.domain.common.exception;

public class ReadOnlyRoleException extends RuntimeException{

    public ReadOnlyRoleException(String message) {
        super(message);
    }
}
