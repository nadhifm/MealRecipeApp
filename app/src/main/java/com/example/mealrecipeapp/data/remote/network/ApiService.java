package com.example.mealrecipeapp.data.remote.network;

import com.example.mealrecipeapp.data.remote.response.GetMealPlanResponse;
import com.example.mealrecipeapp.data.remote.response.MealPlan;
import com.example.mealrecipeapp.data.remote.response.AddMealPlanResponse;
import com.example.mealrecipeapp.data.remote.response.ConnectUserResponse;
import com.example.mealrecipeapp.data.remote.response.GetRecipeResponse;
import com.example.mealrecipeapp.data.remote.response.RecipeInformation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("recipes/complexSearch")
    Call<GetRecipeResponse> getRecipes(
        @Query("apiKey") String apiKey,
        @Query("number") int number,
        @Query("addRecipeInformation") boolean addRecipeInformation,
        @Query("sort") String sort,
        @Query("query") String query
    );

    @GET("recipes/{id}/information")
    Call<RecipeInformation> getRecipeInformation(
        @Path("id") Long id,
        @Query("apiKey") String apiKey
    );

    @POST("users/connect")
    Call<ConnectUserResponse> connectUser(
        @Query("apiKey") String apiKey
    );

    @POST("mealplanner/{username}/items")
    Call<AddMealPlanResponse> addMealPlan(
        @Path("username") String username,
        @Query("hash") String hash,
        @Query("apiKey") String apiKey,
        @Body MealPlan body
    );

    @DELETE("mealplanner/{username}/items/{id}")
    Call<AddMealPlanResponse> deleteMealPlan(
        @Path("username") String username,
        @Path("id") Long id,
        @Query("hash") String hash,
        @Query("apiKey") String apiKey
    );

    @GET("mealplanner/{username}/day/{date}")
    Call<GetMealPlanResponse> getMealPlans(
        @Path("username") String username,
        @Path("date") String date,
        @Query("hash") String hash,
        @Query("apiKey") String apiKey
    );
}
