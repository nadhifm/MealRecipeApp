package com.example.mealrecipeapp.ui.signin;

import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.repository.AppRepository;

public class SignInViewModel extends ViewModel {

    private final AppRepository appRepository;
    public SignInViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void saveUser(String email, String name, String image) {
        appRepository.saveUser(email, name, image);
    }
}
