package com.gamefuse.app.api.model.response

import java.util.Date

data class UserFromBack(
    val _id: String,
    val username: String,
    val firstname: String?,
    val lastname: String?,
    val birthdate: Date?,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val avatar: Avatar?,
)