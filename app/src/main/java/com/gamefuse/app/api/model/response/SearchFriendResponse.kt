package com.gamefuse.app.api.model.response

data class SearchUsersResponse(
    val user: User,
    val isFriend: Boolean
)