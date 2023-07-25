package com.gamefuse.app.api.model.response

data class InvitationsResponse(
    val receiver: User,
    val sender: User,
)