package com.example.mealrecipeapp.data.remote.response;

import java.util.List;

public class GetMealPlanResponse {
    private final List<MealPlan> items;

    public GetMealPlanResponse(List<MealPlan> items) {
        this.items = items;
    }

    public List<MealPlan> getItems() {
        return items;
    }
}
