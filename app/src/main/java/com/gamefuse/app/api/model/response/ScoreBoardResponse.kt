package com.gamefuse.app.api.model.response

data class ScoreboardData(
    val username: String,
    val scores: List<ScoreData>,
    val avatar: AvatarLocation,
)

data class ScoreData(
    val score: Int
)

data class AvatarLocation(
    val location: String
)
