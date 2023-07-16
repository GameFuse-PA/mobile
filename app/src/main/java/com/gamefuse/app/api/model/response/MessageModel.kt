package com.gamefuse.app.api.model.response

data class MessageModel(
    val content: String,
    val from: User,
    val date: Int,
    val conversationId: String
)
