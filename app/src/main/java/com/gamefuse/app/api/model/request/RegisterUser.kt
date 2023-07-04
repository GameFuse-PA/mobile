package com.gamefuse.app.api.model.request

data class RegisterUser(
    val email: String,
    val password: String,
    val username: String
)