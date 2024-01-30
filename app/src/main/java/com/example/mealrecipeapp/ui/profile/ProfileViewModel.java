package com.example.mealrecipeapp.ui.profile;

import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.repository.AppRepository;

public class ProfileViewModel extends ViewModel {
    private final AppRepository appRepository;

    public ProfileViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public String getUserName() {
        return appRepository.getUserName();
    }

    public String getUserEmail() {
        return appRepository.getUserEmail();
    }

    public String getUserImage() {
        return appRepository.getUserImage();
    }

    public void signOut() {
        appRepository.deleteUser();
    }
}
