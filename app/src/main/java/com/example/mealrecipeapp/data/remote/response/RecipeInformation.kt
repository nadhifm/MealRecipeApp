package com.example.mealrecipeapp.data.remote.response

import androidx.annotation.Keep

@Keep
class RecipeInformation(
    val id: Long,
    val title: String,
    val summary: String,
    val image: String,
    val imageType: String,
    val aggregateLikes: Int,
    val readyInMinutes: Int,
    val servings: Int,
    val extendedIngredients: List<Ingredient>,
    val analyzedInstructions: List<AnalyzedInstruction>,
    var isFavorite: Boolean = false
)
