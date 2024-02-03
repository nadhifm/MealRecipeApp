package com.example.mealrecipeapp.ui.mealplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipeapp.data.remote.response.MealPlan
import com.example.mealrecipeapp.data.repository.AppRepository
import com.example.mealrecipeapp.utils.Resource
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MealPlanViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _date = MutableLiveData(Date())
    val date: LiveData<Date>
        get() = _date

     private val _mealPlans = MutableLiveData<Resource<List<MealPlan>>>()
     val mealPlans: LiveData<Resource<List<MealPlan>>>
         get() = _mealPlans

     val messageError = MutableLiveData<String>()
     var deleteMealPlanResult = MutableLiveData<String>()
    fun getMealPlans(newDate: Date?) {
        var date = newDate
        if (newDate == null) {
            date = _date.value
        }
        date?.let {
            viewModelScope.launch {
                _mealPlans.postValue(Resource.Loading())
                try {
                    val listMealPlans = appRepository.getMealPlans(it)
                    _mealPlans.postValue(Resource.Success(listMealPlans))
                } catch (e: Exception) {
                    _mealPlans.postValue(Resource.Error(e.message.toString()))
                }
            }
        }
    }

    fun resetMessageError() {
        messageError.postValue("")
    }

    fun deleteMealPlan(id: Long) {
        viewModelScope.launch {
            try {
                deleteMealPlanResult.postValue(appRepository.deleteMealPlan(id))
            } catch (e: Exception) {
                deleteMealPlanResult.postValue(e.message.toString())
            }
        }
    }

    fun setDate(date: Date) {
        _date.postValue(date)
        getMealPlans(date)
    }

    fun setPrevDate() {
        _date.value?.let {
            val c = Calendar.getInstance()
            c.time = it
            c.add(Calendar.DATE, -1)
            setDate(c.time)
        }
    }

    fun setNextDate() {
        _date.value?.let {
            val c = Calendar.getInstance()
            c.time = it
            c.add(Calendar.DATE, 1)
            setDate(c.time)
        }
    }
}
