package com.example.mealrecipeapp.data.remote.response

import androidx.annotation.Keep

@Keep
class Recipe(
    val id: Long,
    val title: String,
    val image: String,
    val imageType: String,
    val aggregateLikes: Int,
    val readyInMinutes: Int,
    val servings: Int,
    val extendedIngredients: List<Ingredient>,
    val analyzedInstructions: List<AnalyzedInstruction>
)
