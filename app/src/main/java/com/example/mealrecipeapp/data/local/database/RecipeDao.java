package com.example.mealrecipeapp.data.local.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealrecipeapp.data.local.entity.RecipeEntity;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeEntity recipeEntity);

    @Query("SELECT * FROM recipeentity")
    LiveData<List<RecipeEntity>> getFavoriteRecipes();

    @Query("SELECT COUNT(*) FROM recipeentity WHERE id = :id")
    int getFavoriteRecipeById(Long id);

    @Delete
    void delete(RecipeEntity recipeEntity);

    @Query("DELETE FROM recipeentity")
    void deleteAll();
}
