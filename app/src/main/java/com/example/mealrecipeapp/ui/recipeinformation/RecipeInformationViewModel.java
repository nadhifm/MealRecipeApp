package com.example.mealrecipeapp.ui.recipeinformation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.local.entity.RecipeEntity;
import com.example.mealrecipeapp.data.remote.response.RecipeInformation;
import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.utils.Resource;

public class RecipeInformationViewModel extends ViewModel {
    private final AppRepository appRepository;
    private final MutableLiveData<Resource<RecipeInformation>> recipeInformation = new MutableLiveData<>();

    public RecipeInformationViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void getRecipeInformation(Long id) {
        appRepository.getRecipeInformation(id).observeForever(recipeInformation::postValue);
    }

    public LiveData<Resource<RecipeInformation>> getRecipeInformationLiveData() {
        return recipeInformation;
    }

    public void addFavoriteRecipe() {
        RecipeInformation data = recipeInformation.getValue().getData();
        RecipeEntity recipeEntity = new RecipeEntity(data.getID(), data.getTitle(), data.getImage(), data.getImageType(), data.getAggregateLikes(), data.getReadyInMinutes(), data.getServings());
        appRepository.addFavoriteRecipe(recipeEntity);
        data.setFavorite(true);
        recipeInformation.postValue(Resource.success(data));
    }

    public void removeFavoriteRecipe() {
        RecipeInformation data = recipeInformation.getValue().getData();
        RecipeEntity recipeEntity = new RecipeEntity(data.getID(), data.getTitle(), data.getImage(), data.getImageType(), data.getAggregateLikes(), data.getReadyInMinutes(), data.getServings());
        appRepository.removeFavoriteRecipe(recipeEntity);
        data.setFavorite(false);
        recipeInformation.postValue(Resource.success(data));
    }
}
