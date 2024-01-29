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
    private final MutableLiveData<Date> date = new MutableLiveData<>();
    private LiveData<Resource<List<MealPlan>>> mealPlans;
    private final AppRepository appRepository;

    public MealPlanViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        this.mealPlans = this.appRepository.getMealPlans(new Date());
        this.date.postValue(new Date());
    }

    public LiveData<Resource<List<MealPlan>>> getMealPlans() {
        return mealPlans;
    }

    public void deleteMealPlan(Long id) {
        appRepository.deleteMealPlan(id);
    }

    public void setDate(Date date) {
        mealPlans = appRepository.getMealPlans(date);
        this.date.postValue(date);
    }

    public LiveData<Date> getDate() {
        return date;
    }

    public void setPrevDate() {
        if (date.getValue() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date.getValue());

            c.add(Calendar.DATE, -1);
            this.date.postValue(c.getTime());
        }
    }

    public void setNextDate() {
        if (date.getValue() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date.getValue());

            c.add(Calendar.DATE, 1);
            this.date.postValue(c.getTime());
        }
    }
}
