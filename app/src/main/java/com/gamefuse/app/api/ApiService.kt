package com.gamefuse.app.api

import com.gamefuse.app.api.model.request.LoginUser
import com.gamefuse.app.api.model.request.RegisterUser
import com.gamefuse.app.api.model.response.FriendsListResponse
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.api.model.response.ResponseAPISuccess
import com.gamefuse.app.api.model.response.SearchUsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/auth/login")
    suspend fun login(@Body data: LoginUser): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/auth/register")
    suspend fun register(@Body data: RegisterUser): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @GET("/me/friends")
    suspend fun getFriends(@Header("Authorization") token: String): Response<FriendsListResponse>

    @GET("/users")
    suspend fun searchUser(@Header("Authorization") token: String, @Query("search") search: String): Response<List<SearchUsersResponse>>

    @POST("/users/{id}/invite")
    suspend fun addFriend(@Header("Authorization") token: String, @Path("id") id: String): Response<ResponseAPISuccess>


    @Headers("Content-Type: application/json")
    @DELETE("/friends/{id}")
    suspend fun deleteFriend(@Header("Authorization") token: String, @Path("id") id: String): Response<ResponseAPISuccess>


}