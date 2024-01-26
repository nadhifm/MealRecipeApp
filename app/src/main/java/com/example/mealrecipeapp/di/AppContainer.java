package com.example.mealrecipeapp.di;

import android.content.Context;

import com.example.mealrecipeapp.data.remote.network.ApiService;
import com.example.mealrecipeapp.data.repository.AppRepository;
import com.example.mealrecipeapp.ui.ViewModelFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppContainer {

    private Context appContext;

    public AppContainer(Context appContext) {
        this.appContext = appContext;
    }
    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();
    private ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService.class);

    private AppRepository appRepository = new AppRepository(apiService);

    public ViewModelFactory viewModelFactory = new ViewModelFactory(appRepository);
}
