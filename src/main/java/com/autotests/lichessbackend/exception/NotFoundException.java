package com.autotests.lichessbackend.exception;



public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
