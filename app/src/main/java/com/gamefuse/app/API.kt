import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import java.util.Objects
import java.util.concurrent.TimeUnit

data class Example(
    val otherAttribute: String,
    @SerializedName("attributeIWant")
    val attribute: List<String>
)

data class Avatar(
    val location: String,
)

data class User(
    val id: Int,
    @SerializedName("firstname")
    val name: String,
    val email: String,
    val username: String,
    val avatar: Avatar,
)

data class FriendsList(
    @SerializedName("idFriends")
    val friends: List<User>
)

interface API {

    @GET("/example")
    fun example(): Deferred<Example>


    @GET("/friends")
    fun getFriends(@Header("Authorization") token: String): Deferred<FriendsList>

}

object Request {

    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.MINUTES)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("http://localhost:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()
        .create(API::class.java)

    suspend fun addFriend(): Example{
        return api.example().await()
    }

    suspend fun getFriends(token: String): FriendsList{
        return api.getFriends(token).await()
    }

}
