package com.example.mealrecipeapp.data.remote.network

import com.example.mealrecipeapp.data.remote.response.AddMealPlanResponse
import com.example.mealrecipeapp.data.remote.response.ConnectUserResponse
import com.example.mealrecipeapp.data.remote.response.GetMealPlanResponse
import com.example.mealrecipeapp.data.remote.response.GetRecipeResponse
import com.example.mealrecipeapp.data.remote.response.MealPlan
import com.example.mealrecipeapp.data.remote.response.RecipeInformation
import com.example.mealrecipeapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("number") number: Int,
        @Query("sort") sort: String,
        @Query("query") query: String,
        @Query("apiKey") apiKey: String = Constants.API_KEY,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true,
        @Query("instructionsRequired") instructionsRequired: Boolean = true,
        @Query("fillIngredients") fillIngredients: Boolean = true
    ): Response<GetRecipeResponse>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Long,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<RecipeInformation>

    @POST("users/connect")
    suspend fun connectUser(
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<ConnectUserResponse>

    @POST("mealplanner/{username}/items")
    suspend fun addMealPlan(
        @Path("username") username: String,
        @Query("hash") hash: String,
        @Body body: MealPlan,
        @Query("apiKey") apiKey: String = Constants.API_KEY,
    ): Response<AddMealPlanResponse>

    @DELETE("mealplanner/{username}/items/{id}")
    suspend fun deleteMealPlan(
        @Path("username") username: String,
        @Path("id") id: Long,
        @Query("hash") hash: String,
        @Query("apiKey") apiKey: String = Constants.API_KEY,
    ): Response<AddMealPlanResponse>

    @GET("mealplanner/{username}/day/{date}")
    suspend fun getMealPlans(
        @Path("username") username: String,
        @Path("date") date: String,
        @Query("hash") hash: String,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<GetMealPlanResponse>
}
