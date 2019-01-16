package com.dd.chargercounter.View;

public class Product {
    String constantName, constantDefenition, value;

    public Product(String contantName, String constantDefenition, String value) {
        this.constantName = contantName;
        this.constantDefenition = constantDefenition;
        this.value = value;
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    public String getConstantDefenition() {
        return constantDefenition;
    }

    public void setConstantDefenition(String constantDefenition) {
        this.constantDefenition = constantDefenition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
