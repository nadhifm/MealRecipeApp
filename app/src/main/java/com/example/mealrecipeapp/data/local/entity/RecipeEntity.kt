package com.example.mealrecipeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RecipeEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val image: String,
    val imageType: String,
    val aggregateLikes: Int,
    val readyInMinutes: Int,
    val servings: Int
)
