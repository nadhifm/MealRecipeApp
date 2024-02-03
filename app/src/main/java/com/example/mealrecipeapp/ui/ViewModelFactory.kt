package com.example.mealrecipeapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mealrecipeapp.data.repository.AppRepository
import com.example.mealrecipeapp.ui.addmealplan.AddMealPlanViewModel
import com.example.mealrecipeapp.ui.favorite.FavoriteViewModel
import com.example.mealrecipeapp.ui.home.HomeViewModel
import com.example.mealrecipeapp.ui.mealplan.MealPlanViewModel
import com.example.mealrecipeapp.ui.profile.ProfileViewModel
import com.example.mealrecipeapp.ui.recipeinformation.RecipeInformationViewModel
import com.example.mealrecipeapp.ui.signin.SignInViewModel
import com.example.mealrecipeapp.ui.splash.SplashViewModel

class ViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(appRepository) as T
        } else if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            SignInViewModel(appRepository) as T
        } else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            SplashViewModel(appRepository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            ProfileViewModel(appRepository) as T
        } else if (modelClass.isAssignableFrom(MealPlanViewModel::class.java)) {
            MealPlanViewModel(appRepository) as T
        } else if (modelClass.isAssignableFrom(AddMealPlanViewModel::class.java)) {
            AddMealPlanViewModel(appRepository) as T
        } else if (modelClass.isAssignableFrom(RecipeInformationViewModel::class.java)) {
            RecipeInformationViewModel(appRepository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            FavoriteViewModel(appRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
