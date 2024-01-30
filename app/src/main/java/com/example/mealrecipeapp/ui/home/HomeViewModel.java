package com.example.mealrecipeapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.remote.response.Recipe;
import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.utils.Resource;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HomeViewModel extends ViewModel {
    private final AppRepository appRepository;
    private final MutableLiveData<Resource<List<Recipe>>> recipes = new MutableLiveData<>();

    public HomeViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        searchRecipes("");
    }
    public void searchRecipes(String query) {
        appRepository.getRecipes(query).observeForever(recipes::postValue);
    }

    public LiveData<Resource<List<Recipe>>> getRecipesLiveData() {
        return recipes;
    }

    public String getUserName() {
        return appRepository.getUserName();
    }

    public String getUserImage() {
        return appRepository.getUserImage();
    }
}
