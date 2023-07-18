package com.gamefuse.app.api.model.response

import com.google.gson.annotations.SerializedName

data class ConversationModelWithMessages(
    @SerializedName("_id")
    val id: String,
    val users: List<User>,
    val messages: List<MessageModel>,
)
