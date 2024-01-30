package com.example.mealrecipeapp.data.repository;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mealrecipeapp.data.local.database.AppDatabase;
import com.example.mealrecipeapp.data.local.database.RecipeDao;
import com.example.mealrecipeapp.data.local.entity.RecipeEntity;
import com.example.mealrecipeapp.data.remote.network.ApiService;
import com.example.mealrecipeapp.data.remote.response.GetMealPlanResponse;
import com.example.mealrecipeapp.data.remote.response.MealPlan;
import com.example.mealrecipeapp.data.remote.response.AddMealPlanResponse;
import com.example.mealrecipeapp.data.remote.response.MealPlanValue;
import com.example.mealrecipeapp.data.remote.response.ConnectUserResponse;
import com.example.mealrecipeapp.data.remote.response.GetRecipeResponse;
import com.example.mealrecipeapp.data.remote.response.Recipe;
import com.example.mealrecipeapp.data.remote.response.RecipeInformation;
import com.example.mealrecipeapp.utils.Constants;
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
    private final RecipeDao recipeDao;
    public AppRepository(ApiService apiService, SharedPreferences sharedPreferences, RecipeDao recipeDao) {
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
        this.recipeDao = recipeDao;
    }

    public void saveUser(String email, String name, String image) {
        Call<ConnectUserResponse> call = apiService.connectUser(Constants.API_KEY);

        call.enqueue(new Callback<ConnectUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<ConnectUserResponse> call, @NonNull Response<ConnectUserResponse> response) {
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

            }
        });
    }

    public void deleteUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("name");
        editor.apply();

        AppDatabase.databaseWriteExecutor.execute(recipeDao::deleteAll);
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

        Call<GetRecipeResponse> call = apiService.getRecipes(Constants.API_KEY, 25, true, "popularity", query);

        Response<GetRecipeResponse> response = call.execute();
        if (response.body() == null) {
            return new ArrayList<>();
        } else {
            return response.body().getRecipes();
        }
    }

    public LiveData<String> addMealPlan(Long date, int slot, Recipe recipe) {
        final MutableLiveData<String> result = new MutableLiveData<>();

        String username = sharedPreferences.getString("username", "");
        String hash = sharedPreferences.getString("hash", "");

        MealPlanValue valueBody = new MealPlanValue(recipe.getID(), recipe.getServings(), recipe.getTitle(), recipe.getImage(), recipe.getImageType());
        MealPlan body = new MealPlan(0L, date, slot, 0, "RECIPE", valueBody);

        Call<AddMealPlanResponse> call = apiService.addMealPlan(username, hash, Constants.API_KEY, body);

        call.enqueue(new Callback<AddMealPlanResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddMealPlanResponse> call, @NonNull Response<AddMealPlanResponse> response) {
                result.postValue("Success Add Meal Plan");
            }

            @Override
            public void onFailure(@NonNull Call<AddMealPlanResponse> call, @NonNull Throwable t) {
                result.postValue("Fail Add Meal Plan");
            }
        });

        return result;
    }

    public LiveData<Resource<RecipeInformation>> getRecipeInformation(Long id) {
        final MutableLiveData<Resource<RecipeInformation>> result = new MutableLiveData<>();

        result.postValue(Resource.loading(null));

        Call<RecipeInformation> call = apiService.getRecipeInformation(id, Constants.API_KEY);

        call.enqueue(new Callback<RecipeInformation>() {
            @Override
            public void onResponse(@NonNull Call<RecipeInformation> call, @NonNull Response<RecipeInformation> response) {
                if (response.code() == 200 & response.body() != null) {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        RecipeInformation recipeInformation = response.body();
                        int favoriteCount = recipeDao.getFavoriteRecipeById(recipeInformation.getID());
                        if (favoriteCount > 0) {
                            recipeInformation.setFavorite(true);
                        }
                        result.postValue(Resource.success(recipeInformation));
                    });
                } else {
                    result.postValue(Resource.error("Fail Get Recipe Information", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeInformation> call, @NonNull Throwable t) {
                result.postValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<String> deleteMealPlan(Long id) {
        final MutableLiveData<String> result = new MutableLiveData<>();

        String username = sharedPreferences.getString("username", "");
        String hash = sharedPreferences.getString("hash", "");

        Call<AddMealPlanResponse> call = apiService.deleteMealPlan(username, id, hash, Constants.API_KEY);

        call.enqueue(new Callback<AddMealPlanResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddMealPlanResponse> call, @NonNull Response<AddMealPlanResponse> response) {
                result.postValue("Success Delete Meal Plan");
            }

            @Override
            public void onFailure(@NonNull Call<AddMealPlanResponse> call, @NonNull Throwable t) {
                result.postValue("Fail Delete Meal Plan");
            }
        });

        return result;
    }

    public LiveData<Resource<List<MealPlan>>> getMealPlans(Date date) {
        final MutableLiveData<Resource<List<MealPlan>>> mealPlan = new MutableLiveData<>();

        mealPlan.postValue(Resource.loading(null));

        String username = sharedPreferences.getString("username", "");
        String hash = sharedPreferences.getString("hash", "");

        final String dateFormat  = "yyyy-MM-dd";
        final String formattedDate = new SimpleDateFormat(dateFormat, Locale.US).format(date);

        Call<GetMealPlanResponse> call = apiService.getMealPlans(username, formattedDate, hash, Constants.API_KEY);

        call.enqueue(new Callback<GetMealPlanResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetMealPlanResponse> call, @NonNull Response<GetMealPlanResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    mealPlan.postValue(Resource.success(response.body().getItems()));
                } else {
                    mealPlan.postValue(Resource.error("No meals planned for that day", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetMealPlanResponse> call, @NonNull Throwable t) {
                mealPlan.postValue(Resource.error(t.getMessage(), null));
            }
        });

        return mealPlan;
    }

    public void addFavoriteRecipe(RecipeEntity recipeEntity) {
        AppDatabase.databaseWriteExecutor.execute(() -> recipeDao.insert(recipeEntity));
    }

    public void removeFavoriteRecipe(RecipeEntity recipeEntity) {
        AppDatabase.databaseWriteExecutor.execute(() -> recipeDao.delete(recipeEntity));
    }

    public LiveData<List<RecipeEntity>> getFavoriteRecipes() {
        return recipeDao.getFavoriteRecipes();
    }
}
