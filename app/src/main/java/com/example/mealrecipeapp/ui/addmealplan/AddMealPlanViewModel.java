package com.example.mealrecipeapp.ui.addmealplan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.remote.response.Recipe;
import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.utils.Resource;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddMealPlanViewModel extends ViewModel {
    private final AppRepository appRepository;
    private final MutableLiveData<Resource<List<Recipe>>> recipes = new MutableLiveData<>();
    private final MutableLiveData<Resource<String>> addMealPlanResult = new MutableLiveData<>();
    private final MutableLiveData<String> query = new MutableLiveData<>("");

    public AddMealPlanViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        searchRecipes("");
    }

    public void setQuery(String newQuery) {
        if (query.getValue() != null && !query.getValue().equals(newQuery)) {
            query.postValue(newQuery);
            searchRecipes(newQuery);
        }
    }

    private void searchRecipes(String query) {
        appRepository.getRecipes(query).observeForever(recipes::postValue);
    }

    public LiveData<Resource<String>> getAddMealPlanResult() {
        return addMealPlanResult;
    }

    public LiveData<Resource<List<Recipe>>> getRecipesLiveData() {
        return recipes;
    }

    public void addMealPlan(Long date, int slot, Recipe recipe) {
        appRepository.addMealPlan(date, slot, recipe).observeForever(addMealPlanResult::postValue);
    }
}
