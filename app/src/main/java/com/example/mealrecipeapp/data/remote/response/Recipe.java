package com.example.mealrecipeapp.data.remote.response;

import java.util.List;

public class Recipe {
    private final long id;
    private final String title;
    private final String image;
    private final String imageType;
    private final int aggregateLikes;
    private final int readyInMinutes;
    private final int servings;
    private final List<Ingredient> extendedIngredients;
    private final List<AnalyzedInstruction> analyzedInstructions;

    public Recipe(long id, String title, String image, String imageType, int aggregateLikes, int readyInMinutes, int servings, List<Ingredient> extendedIngredients, List<AnalyzedInstruction> analyzedInstructions) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.imageType = imageType;
        this.aggregateLikes = aggregateLikes;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.extendedIngredients = extendedIngredients;
        this.analyzedInstructions = analyzedInstructions;
    }

    public long getID() { return id; }

    public String getTitle() { return title; }

    public String getImage() { return image; }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

    public int getAggregateLikes() {
        return aggregateLikes;
    }

    public List<Ingredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public List<AnalyzedInstruction> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public String getImageType() {
        return imageType;
    }
}
