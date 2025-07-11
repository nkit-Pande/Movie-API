package com.admin.movieApi.exception;

public class FileExistsException extends RuntimeException {
    public FileExistsException(String message) {
        super(message);
    }

}
