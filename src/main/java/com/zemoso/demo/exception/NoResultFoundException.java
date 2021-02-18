package com.zemoso.demo.exception;

public class NoResultFoundException extends RuntimeException {
    public NoResultFoundException(String s) {
        super(s);
    }

    public NoResultFoundException(Throwable throwable) {
        super(throwable);
    }

    public NoResultFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
