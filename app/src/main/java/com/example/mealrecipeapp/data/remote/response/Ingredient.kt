package com.example.mealrecipeapp.data.remote.response

import androidx.annotation.Keep

@Keep
class Ingredient(
    val id: String,
    val name: String,
    val amount: Double,
    val unit: String
)
