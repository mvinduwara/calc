package com.calcapp.core;

public class MemoryStore {

    private double memory = 0.0;
    private boolean hasValue = false;

    public void store(double value) {
        memory = value;
        hasValue = true;
    }

    public double recall() {
        return memory;
    }

    public void add(double value) {
        memory += value;
        hasValue = true;
    }

    public void subtract(double value) {
        memory -= value;
        hasValue = true;
    }

    public void clear() {
        memory = 0.0;
        hasValue = false;
    }

    public boolean hasValue() {
        return hasValue;
    }
}