package com.example.mealrecipeapp.data.remote.response;

public class Recipe {
    private long id;
    private String title;
    private String image;

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public String getImage() { return image; }
    public void setImage(String value) { this.image = value; }
}
