package com.example.mealrecipeapp.ui.splash;

import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.repository.AppRepository;

public class SplashViewModel extends ViewModel {
    private AppRepository appRepository;

    public SplashViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public String checkEmailUser(){
        return appRepository.getEmailUser();
    }
}
