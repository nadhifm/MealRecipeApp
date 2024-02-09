package com.example.mealrecipeapp.data.remote.response

class MealPlan(
    val id: Long,
    val date: Long,
    val slot: Int,
    val position: Int,
    val type: String,
    val value: MealPlanValue
)
