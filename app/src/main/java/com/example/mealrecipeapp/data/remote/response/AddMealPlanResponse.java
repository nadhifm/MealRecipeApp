package com.example.mealrecipeapp.data.remote.response;

public class AddMealPlanResponse {
    private final String status;

    public AddMealPlanResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
