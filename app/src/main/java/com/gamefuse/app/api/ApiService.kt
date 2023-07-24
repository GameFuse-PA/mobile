package com.gamefuse.app.api

import com.gamefuse.app.api.model.request.ForgotPassword
import com.gamefuse.app.api.model.request.Invitations
import com.gamefuse.app.api.model.request.LoginUser
import com.gamefuse.app.api.model.request.RegisterUser
import com.gamefuse.app.api.model.request.UpdateProfil
import com.gamefuse.app.api.model.response.ConversationModelWithMessages
import com.gamefuse.app.api.model.response.ConversationModelWithoutMessages
import com.gamefuse.app.api.model.response.FriendsListResponse
import com.gamefuse.app.api.model.response.InvitationsResponse
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.api.model.response.ResponseAPISuccess
import com.gamefuse.app.api.model.response.ScoreboardData
import com.gamefuse.app.api.model.response.SearchUsersResponse
import com.gamefuse.app.api.model.response.UpdateProfilResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.DELETE
import retrofit2.http.PUT

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

    @Headers("Content-Type: application/json")
    @GET("/me/friends")
    suspend fun getFriends(@Header("Authorization") token: String): Response<FriendsListResponse>

    @GET("/me/invitations")
    suspend fun getInvitations(@Header("Authorization") token: String): Response<List<InvitationsResponse>>

    @GET("/users")
    suspend fun searchUser(@Header("Authorization") token: String, @Query("search") search: String): Response<List<SearchUsersResponse>>

    @POST("/users/{id}/invite")
    suspend fun addFriend(@Header("Authorization") token: String, @Path("id") id: String): Response<ResponseAPISuccess>

    @Headers("Content-Type: application/json")
    @DELETE("/friends/{id}")
    suspend fun deleteFriend(@Header("Authorization") token: String, @Path("id") id: String): Response<ResponseAPISuccess>

    @Headers("Content-Type: application/json")
    @GET("/me/conversations")
    suspend fun getConversations(@Header("Authorization") token: String): Response<List<ConversationModelWithoutMessages>>

    @Headers("Content-Type: application/json")
    @GET("/me/conversations/{id}")
    suspend fun getConversation(@Header("Authorization") token: String, @Path("id") conversationId: String): Response<ConversationModelWithMessages>

    @Headers("Content-Type: application/json")
    @POST("/invitations/accept")
    suspend fun acceptInvitation(@Header("Authorization") token: String, @Body data: Invitations): Response<ResponseAPISuccess>

    @Headers("Content-Type: application/json")
    @POST("/invitations/refuse")
    suspend fun refuseInvitation(@Header("Authorization") token: String, @Body data: Invitations): Response<ResponseAPISuccess>


    @Headers("Content-Type: application/json")
    @PUT("/me")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body data: UpdateProfil): Response<UpdateProfilResponse>

    @GET("/scoreboards/friends")
    suspend fun getScoreBoard(@Header("Authorization") token: String): Response<List<ScoreboardData>>

}