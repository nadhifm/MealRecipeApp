package com.example.mealrecipeapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipeapp.data.remote.response.Recipe
import com.example.mealrecipeapp.data.repository.AppRepository
import com.example.mealrecipeapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _recipes = MutableLiveData<Resource<List<Recipe>>>()
    val recipes: LiveData<Resource<List<Recipe>>>
        get() = _recipes

    private val query = MutableLiveData("")

    private var currentJob: Job? = null

    init {
        searchRecipes("")
    }

    fun setQuery(newQuery: String) {
        if (query.value != newQuery) {
            query.postValue(newQuery)
            searchRecipes(newQuery)
        }
    }

    private fun searchRecipes(query: String) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            delay(800L)
            _recipes.postValue(Resource.Loading())
            try {
                val listRecipes = appRepository.getRecipes(query)
                _recipes.postValue(Resource.Success(listRecipes))
            } catch (e: Exception) {
                _recipes.postValue(Resource.Error(e.message.toString()))
            }
        }
    }
    fun getUserName(): String = appRepository.getUserName()

    fun getUserImage(): String = appRepository.getUserImage()

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }
}
