package com.devs.exceptions;

public class SubjectWithSuchNameExistsException extends Throwable {
    public SubjectWithSuchNameExistsException(String message) {
        super(message);
    }
}
