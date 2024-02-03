package com.example.mealrecipeapp.ui.splash

import androidx.lifecycle.ViewModel
import com.example.mealrecipeapp.data.repository.AppRepository

class SplashViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun checkUserEmail(): String {
        return appRepository.getUserEmail()
    }

    fun checkIsRooted(): Boolean {
        return appRepository.checkIsRooted()
    }

    fun getCheckRootSetting(): Boolean {
        return appRepository.getCheckRootSetting()
    }

    fun checkIsFirstTime() {
        return appRepository.checkIsFirstTime()
    }
}
