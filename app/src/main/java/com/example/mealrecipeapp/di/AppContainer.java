package com.example.mealrecipeapp.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mealrecipeapp.data.remote.network.ApiService;
import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.ui.ViewModelFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppContainer {

    public final ViewModelFactory viewModelFactory;

    public AppContainer(Context appContext) {

        SharedPreferences sharedPreferences = appContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        ApiService apiService = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiService.class);

        AppRepository appRepository = new AppRepository(apiService, sharedPreferences);

        viewModelFactory = new ViewModelFactory(appRepository);
    }
}
