import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.Objects
import java.util.concurrent.TimeUnit

data class Example(
    val otherAttribute: String,
    @SerializedName("attributeIWant")
    val attribute: List<String>
)

data class ResponseAPISuccess(
    val message: String
)

data class LoginUser(
    val email: String,
    val password: String
)

data class UserFromBack(
    val _id: String,
    val username: String,
    val email: String,
    val peerIds: Array<String>,
)

data class LoginResponse(
    val user: UserFromBack,
    val access_token: String,
    val token_type: String,
    val expires_in: String
)

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("NomDeVotreHeader", "ValeurDeVotreHeader")
            .build()
        return chain.proceed(request)
    }
}

interface API {

    @POST("/auth/login")
    fun login(@Body data: LoginUser): Deferred<LoginResponse>


}

object Request {

    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.MINUTES)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl("http://192.168.0.182:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()
        .create(API::class.java)

    suspend fun login(data: LoginUser): LoginResponse {

        val modifiedHttpClient = okHttpClient.newBuilder()
            .addInterceptor(Interceptor { chain ->
                val originalRequest = chain.request()
                val modifiedRequest = originalRequest.newBuilder()
                    .header("NO_AUTH", "true")
                    .build()
                chain.proceed(modifiedRequest)
            })
            .build()

        val loginApi = Retrofit.Builder()
            .baseUrl("http://192.168.0.182:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(modifiedHttpClient)
            .build()
            .create(API::class.java)

        return api.login(data).await()
    }
}
