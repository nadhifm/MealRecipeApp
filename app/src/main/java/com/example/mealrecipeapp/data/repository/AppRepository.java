package com.example.mealrecipeapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mealrecipeapp.data.remote.network.ApiService;
import com.example.mealrecipeapp.data.remote.response.GetRecipeResponse;
import com.example.mealrecipeapp.data.remote.response.Recipe;
import com.example.mealrecipeapp.utils.Resource;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppRepository {
    private ApiService apiService;
    public AppRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes(String query) {
        MutableLiveData<Resource<List<Recipe>>> recipesLiveData = new MutableLiveData<>();

        recipesLiveData.setValue(Resource.loading(null));

        Call<GetRecipeResponse> call = apiService.getRecipes("6ae3d89ec9674c70b31cb4eb99b20889");
        call.enqueue(new Callback<GetRecipeResponse>() {
            @Override
            public void onResponse(Call<GetRecipeResponse> call, Response<GetRecipeResponse> response) {
                if (response.isSuccessful()) {
                    recipesLiveData.setValue(Resource.success(response.body().getRecipes()));
                }
            }

            @Override
            public void onFailure(Call<GetRecipeResponse> call, Throwable t) {
                recipesLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return recipesLiveData;
    }
}
