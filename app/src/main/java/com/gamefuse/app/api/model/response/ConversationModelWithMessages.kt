package com.gamefuse.app.api.model.response

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ConversationModelWithMessages(
    @SerializedName("_id")
    val id: String,
    val users: List<User>,
    val messages: List<MessageModel>,
    val isGameChat: Boolean,
    val createdAt: Date,
    val updatedAt: Date,
)
