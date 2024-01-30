package com.example.mealrecipeapp.ui.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.local.entity.RecipeEntity;
import com.example.mealrecipeapp.data.repository.AppRepository;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private final AppRepository appRepository;

    private final LiveData<List<RecipeEntity>> favoriteRecipes;

    public FavoriteViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        this.favoriteRecipes = this.appRepository.getFavoriteRecipes();
    }

    public LiveData<List<RecipeEntity>> getFavoriteRecipes() {
        return favoriteRecipes;
    }
}
