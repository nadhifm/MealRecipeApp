package com.example.mealrecipeapp.data.remote.response;

public class Recipe {
    private final long id;
    private final String title;
    private final String image;
    private final int aggregateLikes;
    private final int readyInMinutes;
    private final int servings;

    public Recipe(long id, String title, String image, int aggregateLikes, int readyInMinutes, int servings) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.aggregateLikes = aggregateLikes;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
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
}
