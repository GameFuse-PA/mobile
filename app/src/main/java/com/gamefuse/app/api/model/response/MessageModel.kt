package com.gamefuse.app.api.model.response

import java.util.Date

data class MessageModel(
    val content: String,
    val from: User,
    val date: Date,
    val conversationId: String
)
