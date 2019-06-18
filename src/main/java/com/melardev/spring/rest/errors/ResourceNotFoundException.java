package com.melardev.spring.rest.errors;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String id) {
        super("Todo:" + id + " is not found.");
    }
}