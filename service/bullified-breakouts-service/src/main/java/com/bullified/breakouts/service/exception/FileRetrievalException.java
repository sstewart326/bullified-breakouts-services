package com.bullified.breakouts.service.exception;

public class FileRetrievalException extends Exception {

    public FileRetrievalException(String errorMessage) {
        super(errorMessage);
    }

    public FileRetrievalException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
