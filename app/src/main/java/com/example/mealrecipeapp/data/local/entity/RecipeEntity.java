package com.example.mealrecipeapp.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecipeEntity {
    @PrimaryKey()
    private final Long id;
    private final String title;
    private final String image;
    private final String imageType;
    private final int aggregateLikes;
    private final int readyInMinutes;
    private final int servings;

    public RecipeEntity(Long id, String title, String image, String imageType, int aggregateLikes, int readyInMinutes, int servings) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.imageType = imageType;
        this.aggregateLikes = aggregateLikes;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
    }

    public Long getId() {
        return id;
    }

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

    public String getImageType() {
        return imageType;
    }
}
