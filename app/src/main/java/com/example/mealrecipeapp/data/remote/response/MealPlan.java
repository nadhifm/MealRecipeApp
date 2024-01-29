package com.example.mealrecipeapp.data.remote.response;

public class MealPlan {
    private final Long id;
    private final Long date;
    private final int slot;
    private final int position;
    private final String type;
    private final MealPlanValue value;

    public MealPlan(Long id, Long date, int slot, int position, String type, MealPlanValue value) {
        this.id = id;
        this.date = date;
        this.slot = slot;
        this.position = position;
        this.type = type;
        this.value = value;
    }

    public Long getDate() {
        return date;
    }

    public int getSlot() {
        return slot;
    }

    public int getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public MealPlanValue getValue() {
        return value;
    }

    public Long getId() {
        return id;
    }
}
