package com.example.mealrecipeapp.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.ui.addmealplan.AddMealPlanViewModel;
import com.example.mealrecipeapp.ui.favorite.FavoriteViewModel;
import com.example.mealrecipeapp.ui.home.HomeViewModel;
import com.example.mealrecipeapp.ui.mealplan.MealPlanViewModel;
import com.example.mealrecipeapp.ui.profile.ProfileViewModel;
import com.example.mealrecipeapp.ui.recipeinformation.RecipeInformationViewModel;
import com.example.mealrecipeapp.ui.signin.SignInViewModel;
import com.example.mealrecipeapp.ui.splash.SplashViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final AppRepository appRepository;

    public ViewModelFactory(AppRepository appRepository){
        this.appRepository = appRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass, @NonNull CreationExtras extras) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(appRepository);
        } else if (modelClass.isAssignableFrom(SignInViewModel.class)) {
            return (T) new SignInViewModel(appRepository);
        } else if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            return (T) new SplashViewModel(appRepository);
        } else if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(appRepository);
        } else if (modelClass.isAssignableFrom(MealPlanViewModel.class)) {
            return (T) new MealPlanViewModel(appRepository);
        } else if (modelClass.isAssignableFrom(AddMealPlanViewModel.class)) {
            return (T) new AddMealPlanViewModel(appRepository);
        }  else if (modelClass.isAssignableFrom(RecipeInformationViewModel.class)) {
            return (T) new RecipeInformationViewModel(appRepository);
        }   else if (modelClass.isAssignableFrom(FavoriteViewModel.class)) {
            return (T) new FavoriteViewModel(appRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}
