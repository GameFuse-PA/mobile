package com.gamefuse.app.api.model.response

import com.google.gson.annotations.SerializedName

data class Avatar(
    var location: String,
)

data class User(
    @SerializedName("_id")
    val id: String,
    @SerializedName("firstname")
    val name: String,
    val email: String,
    val username: String,
    var avatar: Avatar?,
)

data class FriendsListResponse(
    val friends: List<User>
)

data class ResponseAPISuccess(
    val message: String
)