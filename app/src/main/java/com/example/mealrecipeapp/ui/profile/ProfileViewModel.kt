package com.example.mealrecipeapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipeapp.data.repository.AppRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun getUserName(): String = appRepository.getUserName()
    fun getUserEmail(): String = appRepository.getUserEmail()
    fun getUserImage(): String = appRepository.getUserImage()
    fun getCheckRootSetting(): Boolean = appRepository.getCheckRootSetting()

    fun setCheckRootSetting(isChecked: Boolean) {
        appRepository.setCheckRootSetting(isChecked)
    }

    fun getCheckEmulatorSetting(): Boolean = appRepository.getCheckEmulatorSetting()

    fun setCheckEmulatorSetting(isChecked: Boolean) {
        appRepository.setCheckEmulatorSetting(isChecked)
    }

    fun getCheckUSBDebugSetting(): Boolean = appRepository.getCheckUSBDebugSetting()

    fun setCheckUSBDebugSetting(isChecked: Boolean) {
        appRepository.setCheckUSBDebugSetting(isChecked)
    }

    fun getCheckAccessibilitySetting(): Boolean = appRepository.getCheckAccessibilitySetting()

    fun setCheckAccessibilitySetting(isChecked: Boolean) {
        appRepository.setCheckAccessibilitySetting(isChecked)
    }

    fun signOut() {
        viewModelScope.launch {
            appRepository.deleteUser()
        }
    }
}
