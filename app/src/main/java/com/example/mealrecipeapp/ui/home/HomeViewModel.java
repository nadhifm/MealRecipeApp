package com.example.mealrecipeapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.remote.response.Recipe;
import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.utils.Resource;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private AppRepository appRepository;
    private LiveData<Resource<List<Recipe>>> recipes;

    public HomeViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        this.recipes = this.appRepository.getRecipes("");
    }

    public LiveData<Resource<List<Recipe>>> getRecipesLiveData() {
        return recipes;
    }
}
