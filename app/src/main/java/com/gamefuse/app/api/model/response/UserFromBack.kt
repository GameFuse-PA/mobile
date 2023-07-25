package com.gamefuse.app.api.model.response

import java.time.LocalDate

data class UserFromBack(
    val _id: String,
    val username: String,
    val firstname: String?,
    val lastname: String?,
    val birthdate: String?,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val avatar: Avatar?,
)