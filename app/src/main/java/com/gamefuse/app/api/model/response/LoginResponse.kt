package com.gamefuse.app.api.model.response

data class LoginResponse(
    val user: UserFromBack,
    val access_token: String,
    val token_type: String,
    val expires_in: String
)