package com.gamefuse.app.api

import com.gamefuse.app.api.model.request.ForgotPassword
import com.gamefuse.app.api.model.request.LoginUser
import com.gamefuse.app.api.model.request.RegisterUser
import com.gamefuse.app.api.model.response.LoginResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/auth/login")
    suspend fun login(@Body data: LoginUser): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/auth/register")
    suspend fun register(@Body data: RegisterUser): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/auth/forgot-password")
    suspend fun forgotPassword(@Body data: ForgotPassword): Response<LoginResponse>

}