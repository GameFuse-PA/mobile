package com.gamefuse.app.api.model.response

data class ScoreboardData(
    val _id: String,
    val username: String,
    val email: String,
    val friends: List<UserData>,
    val scores: List<ScoreData>,
    val createdAt: String,
    val updatedAt: String,
    val avatar: Avatar,
    val birthdate: String,
    val firstname: String,
    val lastname: String
)

data class UserData(
    val _id: String,
    val username: String,
    val email: String,
    val friends: List<String>,
    val scores: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val avatar: String,
    val birthdate: String,
    val firstname: String,
    val lastname: String
)

data class ScoreData(
    val _id: String,
    val game: String,
    val score: Int,
    val createdAt: String,
    val updatedAt: String
)
