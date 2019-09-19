package com.space.service;

import org.jetbrains.annotations.Contract;

public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;

    @Contract(pure = true)
    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }
}