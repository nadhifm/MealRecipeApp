package com.example.mealrecipeapp.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipeapp.data.repository.AppRepository
import com.example.mealrecipeapp.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _signInResult = MutableLiveData<Resource<String>>()
    val signInResult: LiveData<Resource<String>>
        get() = _signInResult
    fun saveUser() {
        viewModelScope.launch {
            try {
                delay(500L)
                val message = appRepository.saveUser()
                _signInResult.postValue(Resource.Success(message))
            } catch (e: Exception) {
                _signInResult.postValue(Resource.Error(e.message.toString()))
            }
        }
    }
}
