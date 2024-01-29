package com.example.mealrecipeapp.data.remote.response;

public class Ingredient {
    private final String id;
    private final String name;
    private final Double amount;
    private final String unit;

    public Ingredient(String id, String name, Double amount, String unit) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
}
