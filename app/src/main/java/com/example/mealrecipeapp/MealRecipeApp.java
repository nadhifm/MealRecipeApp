package com.example.mealrecipeapp;

import android.app.Application;

import com.example.mealrecipeapp.di.AppContainer;

public class MealRecipeApp extends Application {
    public AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer(this);
    }
}
