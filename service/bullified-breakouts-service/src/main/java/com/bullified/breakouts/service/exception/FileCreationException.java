package com.bullified.breakouts.service.exception;

public class FileCreationException extends Exception {

    public FileCreationException(String errorMessage) {
        super(errorMessage);
    }

    public FileCreationException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
