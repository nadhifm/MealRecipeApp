package com.example.mealrecipeapp.data.remote.response

import androidx.annotation.Keep

@Keep
class ConnectUserResponse(
    val username: String,
    val hash: String
)
