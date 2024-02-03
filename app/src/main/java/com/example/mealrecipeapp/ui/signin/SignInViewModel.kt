package com.example.mealrecipeapp.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipeapp.data.repository.AppRepository
import com.example.mealrecipeapp.utils.Resource
import kotlinx.coroutines.launch

class SignInViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _signInResult = MutableLiveData<Resource<String>>()
    val signInResult: LiveData<Resource<String>>
        get() = _signInResult
    fun saveUser(email: String, name: String, image: String) {
        viewModelScope.launch {
            _signInResult.postValue(Resource.Loading())
            try {
                _signInResult.postValue(Resource.Success(appRepository.saveUser(email, name, image)))
            } catch (e: Exception) {
                _signInResult.postValue(Resource.Error(e.message.toString()))
            }
        }
    }
}
