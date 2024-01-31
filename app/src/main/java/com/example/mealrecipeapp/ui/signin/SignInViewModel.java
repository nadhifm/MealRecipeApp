package com.example.mealrecipeapp.ui.signin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.utils.Resource;

public class SignInViewModel extends ViewModel {

    private final AppRepository appRepository;
    private final MutableLiveData<Resource<String>> signInResult = new MutableLiveData<>();
    public SignInViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void saveUser(String email, String name, String image) {
        appRepository.saveUser(email, name, image).observeForever(signInResult::postValue);
    }

    public LiveData<Resource<String>> getSignInResult() {
        return signInResult;
    }
}
