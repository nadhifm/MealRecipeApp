package com.example.mealrecipeapp.data.remote.response;

import java.util.List;

public class GetRecipeResponse {
    private List<Recipe> results;

    public List<Recipe> getRecipes() { return results; }
    public void setRecipes(List<Recipe> value) { this.results = value; }
}
