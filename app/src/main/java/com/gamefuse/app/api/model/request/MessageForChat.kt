package com.gamefuse.app.api.model.request

data class MessageForChat(
    var content: String,
    val to: String,
    val conversationId: String,
)