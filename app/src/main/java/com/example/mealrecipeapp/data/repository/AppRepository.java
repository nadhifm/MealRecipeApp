package com.example.mealrecipeapp.data.repository;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mealrecipeapp.data.remote.network.ApiService;
import com.example.mealrecipeapp.data.remote.response.GetMealPlanResponse;
import com.example.mealrecipeapp.data.remote.response.MealPlan;
import com.example.mealrecipeapp.data.remote.response.AddMealPlanResponse;
import com.example.mealrecipeapp.data.remote.response.MealPlanValue;
import com.example.mealrecipeapp.data.remote.response.ConnectUserResponse;
import com.example.mealrecipeapp.data.remote.response.GetRecipeResponse;
import com.example.mealrecipeapp.data.remote.response.Recipe;
import com.example.mealrecipeapp.utils.Resource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppRepository {
    private final ApiService apiService;
    private final SharedPreferences sharedPreferences;
    public AppRepository(ApiService apiService, SharedPreferences sharedPreferences) {
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
    }

    public void saveUser(String email, String name, String image) {
        Call<ConnectUserResponse> call = apiService.connectUser("f69404291ca9448ab677f16be1fbb544");

        call.enqueue(new Callback<ConnectUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<ConnectUserResponse> call, Response<ConnectUserResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.putString("name", name);
                    editor.putString("image", image);
                    editor.putString("username", response.body().getUsername());
                    editor.putString("hash", response.body().getHash());
                    editor.apply();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ConnectUserResponse> call, @NonNull Throwable t) {

                Log.d("//ERROR//", t.getMessage());
            }
        });
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

        Call<GetRecipeResponse> call = apiService.getRecipes("f69404291ca9448ab677f16be1fbb544", 25, true, true, "popularity", query);

        Response<GetRecipeResponse> response = call.execute();
        if (response.body() == null) {
            return new ArrayList<>();
        } else {
            return response.body().getRecipes();
        }
    }

    public void addMealPlan(Long date, int slot, Recipe recipe) throws IOException {

        String username = sharedPreferences.getString("username", "");
        String hash = sharedPreferences.getString("hash", "");

        MealPlanValue valueBody = new MealPlanValue(recipe.getID(), recipe.getServings(), recipe.getTitle(), recipe.getImage(), recipe.getImageType());
        MealPlan body = new MealPlan(0L, date, slot, 0, "RECIPE", valueBody);

        Call<AddMealPlanResponse> call = apiService.addMealPlan(username, hash, "f69404291ca9448ab677f16be1fbb544", body);

        call.enqueue(new Callback<AddMealPlanResponse>() {
            @Override
            public void onResponse(Call<AddMealPlanResponse> call, Response<AddMealPlanResponse> response) {

            }

            @Override
            public void onFailure(Call<AddMealPlanResponse> call, Throwable t) {

            }
        });

    }

    public void deleteMealPlan(Long id) {

        String username = sharedPreferences.getString("username", "");
        String hash = sharedPreferences.getString("hash", "");

        Call<AddMealPlanResponse> call = apiService.deleteMealPlan(username, id, hash, "f69404291ca9448ab677f16be1fbb544");

        call.enqueue(new Callback<AddMealPlanResponse>() {
            @Override
            public void onResponse(Call<AddMealPlanResponse> call, Response<AddMealPlanResponse> response) {

            }

            @Override
            public void onFailure(Call<AddMealPlanResponse> call, Throwable t) {

            }
        });

    }

    public LiveData<Resource<List<MealPlan>>> getMealPlans(Date date) {
        final MutableLiveData<Resource<List<MealPlan>>> mealPlan = new MutableLiveData<>();

        mealPlan.postValue(Resource.loading(null));

        String username = sharedPreferences.getString("username", "");
        String hash = sharedPreferences.getString("hash", "");

        final String dateFormat  = "yyyy-MM-dd";
        final String formattedDate = new SimpleDateFormat(dateFormat, Locale.US).format(date);

        Call<GetMealPlanResponse> call = apiService.getMealPlans(username, formattedDate, hash, "f69404291ca9448ab677f16be1fbb544");

        call.enqueue(new Callback<GetMealPlanResponse>() {
            @Override
            public void onResponse(Call<GetMealPlanResponse> call, Response<GetMealPlanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mealPlan.postValue(Resource.success(response.body().getItems()));
                }
            }

            @Override
            public void onFailure(Call<GetMealPlanResponse> call, Throwable t) {
                mealPlan.postValue(Resource.error(t.getMessage(), null));
            }
        });

        return mealPlan;
    }
}
