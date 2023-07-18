package com.gamefuse.app.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL: String ="http://192.168.14.120:3000"
    //Noe PC fixe"http://192.168.0.182:3000"
    //Noe PC portable : "http://192.168.184.81:3000"

    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    val apiService : ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }
}