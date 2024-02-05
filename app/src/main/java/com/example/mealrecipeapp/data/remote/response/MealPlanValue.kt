package com.example.mealrecipeapp.data.remote.response

import androidx.annotation.Keep

@Keep
class MealPlanValue(
    val id: Long,
    val servings: Int,
    val title: String,
    val image: String
)
