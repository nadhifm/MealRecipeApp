package com.example.mealrecipeapp.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mealrecipeapp.data.local.entity.RecipeEntity

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM recipeentity")
    fun favoriteRecipes(): LiveData<List<RecipeEntity>>

    @Query("SELECT COUNT(*) FROM recipeentity WHERE id = :id")
    fun getFavoriteRecipeById(id: Long): Int

    @Delete
    suspend fun delete(recipeEntity: RecipeEntity)

    @Query("DELETE FROM recipeentity")
    suspend fun deleteAll()
}
