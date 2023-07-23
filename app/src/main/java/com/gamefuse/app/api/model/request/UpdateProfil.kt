package com.gamefuse.app.api.model.request

data class UpdateProfil(
    val email: String,
    val firstname: String,
    val birthdate: String,
    val lastname: String,
) {
}