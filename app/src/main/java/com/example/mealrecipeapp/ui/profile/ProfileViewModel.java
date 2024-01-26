package com.example.mealrecipeapp.ui.profile;

import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.repository.AppRepository;

public class ProfileViewModel extends ViewModel {
    private AppRepository appRepository;

    public ProfileViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void signOut() {
        appRepository.deleteUser();
    }
}
