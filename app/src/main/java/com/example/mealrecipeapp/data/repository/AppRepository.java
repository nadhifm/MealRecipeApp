package com.example.mealrecipeapp.data.repository;

import android.content.SharedPreferences;

import com.example.mealrecipeapp.data.remote.network.ApiService;
import com.example.mealrecipeapp.data.remote.response.GetRecipeResponse;
import com.example.mealrecipeapp.data.remote.response.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AppRepository {
    private final ApiService apiService;
    private final SharedPreferences sharedPreferences;
    public AppRepository(ApiService apiService, SharedPreferences sharedPreferences) {
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
    }

    public void saveUser(String email, String name, String image) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("name", name);
        editor.putString("image", image);
        editor.apply();
    }

    public void deleteUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("name");
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString("email", "Email");
    }

    public String getUserName() {
        return sharedPreferences.getString("name", "Name");
    }
    public String getUserImage() {
        return sharedPreferences.getString("image", "");
    }

    public List<Recipe> getRecipes(String query) throws IOException {

        Call<GetRecipeResponse> call = apiService.getRecipes("6ae3d89ec9674c70b31cb4eb99b20889", 25, true, "popularity", query);

        Response<GetRecipeResponse> response = call.execute();
        if (response.body() == null) {
            return new ArrayList<>();
        } else {
            return response.body().getRecipes();
        }
    }
}
