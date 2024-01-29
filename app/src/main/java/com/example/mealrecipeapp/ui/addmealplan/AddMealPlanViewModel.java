package com.example.mealrecipeapp.ui.addmealplan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.remote.response.Recipe;
import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.utils.Resource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddMealPlanViewModel extends ViewModel {
    private final AppRepository appRepository;
    private final MutableLiveData<Resource<List<Recipe>>> recipes = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<?> currentJob;

    public AddMealPlanViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        searchRecipes("");
    }
    public void searchRecipes(String query) {
        if (currentJob != null && !currentJob.isDone()) {
            currentJob.cancel(true);
        }

        currentJob = executorService.submit((Callable<Void>) () -> {
            Thread.sleep(500L);
            recipes.postValue(Resource.loading(null));
            try {
                List<Recipe> recipeList = appRepository.getRecipes(query);
                recipes.postValue(Resource.success(recipeList));
            } catch (IOException e) {
                recipes.postValue(Resource.error(e.getMessage(), null));
            }
            return null;
        });
    }

    public LiveData<Resource<List<Recipe>>> getRecipesLiveData() {
        return recipes;
    }

    public void addMealPlan(Long date, int slot, Recipe recipe) {
        try {
            appRepository.addMealPlan(date, slot, recipe);
        } catch (IOException e) {
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
