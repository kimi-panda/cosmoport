package com.space.controller;

import org.jetbrains.annotations.Contract;

public enum ShipOrder {
    ID("id"), // default
    SPEED("speed"),
    DATE("prodDate"),
    RATING("rating");

    private String fieldName;

    @Contract(pure = true)
    ShipOrder(String fieldName) {
        this.fieldName = fieldName;
    }

    @Contract(pure = true)
    public String getFieldName() {
        return fieldName;
    }
}