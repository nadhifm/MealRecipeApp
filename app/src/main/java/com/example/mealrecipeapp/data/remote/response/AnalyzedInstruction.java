package com.example.mealrecipeapp.data.remote.response;

import java.util.List;

public class AnalyzedInstruction {
    private final List<Instruction> steps;

    public AnalyzedInstruction(List<Instruction> steps) {
        this.steps = steps;
    }

    public List<Instruction> getSteps() {
        return steps;
    }
}
