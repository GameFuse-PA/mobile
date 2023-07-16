package com.gamefuse.app.api.model.response

data class ConversationModel(
    val _id: String,
    val users: List<User>,
    val messages: List<MessageModel>,
    val conversationId: String
)
