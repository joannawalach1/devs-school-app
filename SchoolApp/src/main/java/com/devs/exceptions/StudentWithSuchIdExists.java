package com.devs.exceptions;

public class StudentWithSuchIdExists extends Exception {
    public StudentWithSuchIdExists(String message) {
        super(message);
    }
}
