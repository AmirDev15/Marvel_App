package com.example.marvel_app.data.framework.network

import android.util.Log
import com.example.marvel_app.data.data_source.remote.ApiService.MarvelApiService
import com.example.marvel_app.data.framework.util.generateHash

import okhttp3.OkHttpClient
import okhttp3.HttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {

    private const val BASE_URL = "https://gateway.marvel.com/"
    public const val PUBLIC_KEY = "0de055665d65a7da0a64fc7e494ed135"
    private const val PRIVATE_KEY = "9c3de5724690bf008ef09484f4e6cf283b6707b9"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(120L, TimeUnit.SECONDS)
        .readTimeout(120L, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            // Dynamically generate ts and hash
            val ts = System.currentTimeMillis().toString()
            val hash = generateHash(ts, PRIVATE_KEY, PUBLIC_KEY)


            val url: HttpUrl = originalUrl.newBuilder()
                .apply {
                    if (!originalUrl.queryParameterNames.contains("ts")) {
                        addQueryParameter("ts", ts)
                    }
                    if (!originalUrl.queryParameterNames.contains("apikey")) {
                        addQueryParameter("apikey", PUBLIC_KEY)
                    }
                    if (!originalUrl.queryParameterNames.contains("hash")) {
                        addQueryParameter("hash", hash)
                    }
                }
                .build()
            Log.d("marvel_search", "Final URL: $url}")

            val request = originalRequest.newBuilder().url(url).build()
            chain.proceed(request)

        }

        .build()


    private val retrofit: Retrofit = Retrofit.Builder()

        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService: MarvelApiService = retrofit.create(MarvelApiService::class.java)
}


