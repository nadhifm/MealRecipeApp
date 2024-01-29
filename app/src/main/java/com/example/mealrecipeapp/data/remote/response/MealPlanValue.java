package com.example.mealrecipeapp.data.remote.response;

public class MealPlanValue {
    private final Long id;
    private final int servings;
    private final String title;
    private final String image;
    private final String imageType;

    public MealPlanValue(Long id, int servings, String title, String image, String imageType) {
        this.id = id;
        this.servings = servings;
        this.title = title;
        this.image = image;
        this.imageType = imageType;
    }

    public Long getId() {
        return id;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public String getImageType() {
        return imageType;
    }

    public String getTitle() {
        return title;
    }
}
