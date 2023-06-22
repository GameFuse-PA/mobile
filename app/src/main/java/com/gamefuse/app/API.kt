import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.Objects
import java.util.concurrent.TimeUnit

data class Example(
    val otherAttribute: String,
    @SerializedName("attributeIWant")
    val attribute: List<String>
)

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

interface API {

    @GET("/example")
    fun example(): Deferred<Example>


    @GET("/me/friends")
    fun getFriends(@Header("Authorization") token: String): Deferred<FriendsList>


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

    suspend fun addFriend(): Example{
        return api.example().await()
    }

    suspend fun getFriends(token: String): FriendsList{
        return api.getFriends("Bearer $token").await()
    }

    suspend fun deleteFriend(token: String, id: String): ResponseAPISuccess {
        return api.deleteFriend("Bearer $token", id).await()
    }

}
