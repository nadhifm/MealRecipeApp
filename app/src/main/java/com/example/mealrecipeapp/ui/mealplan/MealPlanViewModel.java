package com.example.mealrecipeapp.ui.mealplan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealrecipeapp.data.remote.response.MealPlan;
import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.utils.Resource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MealPlanViewModel extends ViewModel {
    private final MutableLiveData<Date> date = new MutableLiveData<>(new Date());
    private final MutableLiveData<Resource<List<MealPlan>>> mealPlans = new MutableLiveData<>();
    private final MutableLiveData<String> messageError = new MutableLiveData<>();
    private final MutableLiveData<String> deleteMealPlanResult = new MutableLiveData<>();
    private final AppRepository appRepository;

    public MealPlanViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void getMealPlans(Date date) {
        if (date == null) {
            date = getDate().getValue();
        }
        deleteMealPlanResult.postValue("");
        appRepository.getMealPlans(date).observeForever(resource -> {
            if (resource.getStatus() == Resource.Status.ERROR) {
                messageError.postValue(resource.getMessage());
            }

            mealPlans.postValue(resource);
        });
    }

    public LiveData<Resource<List<MealPlan>>> getMealPlansLiveData() {
        return mealPlans;
    }

    public LiveData<String> getMessageErrorLiveData() {
        return messageError;
    }

    public void resetMessageError() {
        messageError.postValue("");
    }

    public LiveData<String> getDeleteMealPlanResultLiveData() {
        return deleteMealPlanResult;
    }

    public void deleteMealPlan(Long id) {
        appRepository.deleteMealPlan(id).observeForever(deleteMealPlanResult::postValue);
    }

    public void setDate(Date date) {
        this.date.postValue(date);
        getMealPlans(date);
    }

    public LiveData<Date> getDate() {
        return date;
    }

    public void setPrevDate() {
        if (date.getValue() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date.getValue());

            c.add(Calendar.DATE, -1);
            setDate(c.getTime());
        }
    }

    public void setNextDate() {
        if (date.getValue() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date.getValue());

            c.add(Calendar.DATE, 1);
            setDate(c.getTime());
        }
    }
}
