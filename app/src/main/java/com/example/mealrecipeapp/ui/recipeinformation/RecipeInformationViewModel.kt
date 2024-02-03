package com.example.mealrecipeapp.ui.recipeinformation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipeapp.data.local.entity.RecipeEntity
import com.example.mealrecipeapp.data.remote.response.RecipeInformation
import com.example.mealrecipeapp.data.repository.AppRepository
import com.example.mealrecipeapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeInformationViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _recipeInformation = MutableLiveData<Resource<RecipeInformation>>()
    val recipeInformation: LiveData<Resource<RecipeInformation>>
        get() = _recipeInformation
    fun getRecipeInformation(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _recipeInformation.postValue(Resource.Loading())
            try {
                val recipeInformation = appRepository.getRecipeInformation(id)
                _recipeInformation.postValue(Resource.Success(recipeInformation))
            } catch (e: Exception) {
                _recipeInformation.postValue(Resource.Error(e.message.toString()))
            }
        }
    }
    fun addFavoriteRecipe() {
        val data = recipeInformation.value?.data
        data?.let { recipe ->
            viewModelScope.launch {
                val recipeEntity = RecipeEntity(
                    recipe.id,
                    recipe.title,
                    recipe.image,
                    recipe.imageType,
                    recipe.aggregateLikes,
                    recipe.readyInMinutes,
                    recipe.servings
                )
                appRepository.addFavoriteRecipe(recipeEntity)
                recipe.isFavorite = true
                _recipeInformation.postValue(Resource.Success(recipe))
            }
        }
    }

    fun removeFavoriteRecipe() {
        val data = recipeInformation.value?.data
        data?.let { recipe ->
            viewModelScope.launch {
                val recipeEntity = RecipeEntity(
                    recipe.id,
                    recipe.title,
                    recipe.image,
                    recipe.imageType,
                    recipe.aggregateLikes,
                    recipe.readyInMinutes,
                    recipe.servings
                )
                appRepository.removeFavoriteRecipe(recipeEntity)
                recipe.isFavorite = false
                _recipeInformation.postValue(Resource.Success(recipe))
            }
        }
    }
}
