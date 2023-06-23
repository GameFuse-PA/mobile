package com.gamefuse.app

import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class Avatar(
    var location: String,
)

data class User(
    @SerializedName("_id")
    val id: String,
    @SerializedName("firstname")
    val name: String,
    val email: String,
    val username: String,
    var avatar: Avatar?,
)

data class FriendsList(
    @SerializedName("idFriends")
    val friends: List<User>
)

data class ResponseAPISuccess(
    val message: String
)

data class RequestAPIAddFriend(
    @SerializedName("idFriends")
    val id: String
)

interface API {


    @GET("/me/friends")
    fun getFriends(@Header("Authorization") token: String): Deferred<FriendsList>

    @GET("/users")
    fun searchUser(@Header("Authorization") token: String, @Query("search") search: String): Deferred<List<User>>

    @POST("/friends")
    fun addFriend(@Header("Authorization") token: String, @Body id: RequestAPIAddFriend): Deferred<ResponseAPISuccess>

    @DELETE("/friends/{id}")
    fun deleteFriend(@Header("Authorization") token: String, @Path("id") id: String): Deferred<ResponseAPISuccess>

}

object Request {

    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.MINUTES)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("http://192.168.1.105:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()
        .create(API::class.java)

    suspend fun getFriends(token: String): FriendsList {
        return api.getFriends("Bearer $token").await()
    }

    suspend fun searchUser(token: String, search: String): List<User> {
        return api.searchUser("Bearer $token", search).await()
    }

    suspend fun addFriend(token: String, id: String): ResponseAPISuccess {
        return api.addFriend("Bearer $token", RequestAPIAddFriend(id)).await()
    }

    suspend fun deleteFriend(token: String, id: String): ResponseAPISuccess {
        return api.deleteFriend("Bearer $token", id).await()
    }

}
