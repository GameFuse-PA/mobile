package com.gamefuse.app.api.model.response

data class UserFromBack(
    val _id: String,
    val username: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String
)