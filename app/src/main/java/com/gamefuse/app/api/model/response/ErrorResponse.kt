package com.gamefuse.app.api.model.response

data class ErrorResponse(
    val statusCode: Int,
    val message: String,
    val error: String
)
