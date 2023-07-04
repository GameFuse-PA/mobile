package com.gamefuse.app.api.model.response

data class ErrorResponseWithArrayMessage(
    val statusCode: Int,
    val message: List<String>,
    val error: String
)
