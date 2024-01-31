package com.example.mealrecipeapp.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.remote.response.Recipe;
import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.utils.Resource;

import java.util.List;
import java.util.Objects;

public class HomeViewModel extends ViewModel {
    private final AppRepository appRepository;
    private final MutableLiveData<Resource<List<Recipe>>> recipes = new MutableLiveData<>();
    private final MutableLiveData<String> query = new MutableLiveData<>("");

    public HomeViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        searchRecipes("");
    }

    public void setQuery(String newQuery) {
        if (query.getValue() != null &&!query.getValue().equals(newQuery) && !newQuery.equals("")) {
            query.postValue(newQuery);
            searchRecipes(newQuery);
        }
    }
    private void searchRecipes(String query) {
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
