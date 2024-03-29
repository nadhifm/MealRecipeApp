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

    fun checkIsEmulator(): Boolean {
        return appRepository.checkIsEmulator()
    }

    fun getCheckEmulatorSetting(): Boolean {
        return appRepository.getCheckEmulatorSetting()
    }

    fun checkIsUSBDebugEnable(): Boolean {
        return appRepository.checkIsUSBDebugEnable()
    }

    fun getCheckUSBDebugSetting(): Boolean {
        return appRepository.getCheckUSBDebugSetting()
    }

    fun checkIsAccessibilityEnable(): Boolean {
        return appRepository.checkIsAccessibilityEnable()
    }

    fun getCheckAccessibilitySetting(): Boolean {
        return appRepository.getCheckAccessibilitySetting()
    }
}
