package com.example.mealrecipeapp.data.remote.response

import androidx.annotation.Keep

@Keep
class GetRecipeResponse(
    var results: List<Recipe>
)
