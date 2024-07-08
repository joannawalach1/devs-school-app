package com.devs.exceptions;

public class ExamWithSuchNameExistsException extends Throwable {
    public ExamWithSuchNameExistsException(String message) {
        super(message);
    }
}
