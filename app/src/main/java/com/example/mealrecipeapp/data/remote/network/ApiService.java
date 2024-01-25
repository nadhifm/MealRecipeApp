package com.example.mealrecipeapp.data.remote.network;

import com.example.mealrecipeapp.data.remote.response.GetRecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("recipes/complexSearch")
    Call<GetRecipeResponse> getRecipes(
        @Query("apiKey") String apiKey
    );
}
