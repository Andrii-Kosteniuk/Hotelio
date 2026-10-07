package com.hotelio.common.exception;

public class RequestParameterNotValidException extends RuntimeException {

    public RequestParameterNotValidException(String message) {
        super(message);
    }
}
