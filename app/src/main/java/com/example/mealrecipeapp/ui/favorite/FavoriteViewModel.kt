package com.example.mealrecipeapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mealrecipeapp.data.local.entity.RecipeEntity
import com.example.mealrecipeapp.data.repository.AppRepository

class FavoriteViewModel(appRepository: AppRepository) : ViewModel() {
    val favoriteRecipes: LiveData<List<RecipeEntity>> = appRepository.getFavoriteRecipes()
}
