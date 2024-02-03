package com.example.mealrecipeapp

import android.app.Application
import com.example.mealrecipeapp.di.AppContainer

class MealRecipeApp : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
