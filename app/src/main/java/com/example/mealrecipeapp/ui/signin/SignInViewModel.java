package com.example.mealrecipeapp.ui.signin;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.repository.AppRepository;

public class SignInViewModel extends ViewModel {

    private AppRepository appRepository;
    public SignInViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void saveUser(String email, String name) {
        appRepository.saveUser(email, name);
    }
}
