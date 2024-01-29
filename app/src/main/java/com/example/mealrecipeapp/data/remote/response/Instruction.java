package com.example.mealrecipeapp.data.remote.response;

public class Instruction {
    private final int number;
    private final String step;

    public Instruction(int number, String step) {
        this.number = number;
        this.step = step;
    }

    public int getNumber() {
        return number;
    }

    public String getStep() {
        return step;
    }
}
