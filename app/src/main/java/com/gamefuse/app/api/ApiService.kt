package com.gamefuse.app.api

import com.gamefuse.app.api.model.request.LoginUser
import com.gamefuse.app.api.model.response.LoginResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/auth/login")
    suspend fun login(@Body data: LoginUser): Response<LoginResponse>

}