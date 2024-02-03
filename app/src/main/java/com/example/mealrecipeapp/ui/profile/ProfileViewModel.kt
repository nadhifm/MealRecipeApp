package com.example.mealrecipeapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipeapp.data.repository.AppRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun getUserName(): String = appRepository.getUserName()
    fun getUserEmail(): String = appRepository.getUserEmail()
    fun getUserImage(): String = appRepository.getUserImage()

    fun signOut() {
        viewModelScope.launch {
            appRepository.deleteUser()
        }
    }
}
