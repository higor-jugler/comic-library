package com.endeavorsheep.comiclibrary.model.api

import com.endeavorsheep.comiclibrary.BuildConfig
import com.endeavorsheep.comiclibrary.getHash
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "http://gateway.marvel.com/V1/public/"

    private fun getRetrofit(): Retrofit {
        val ts = System.currentTimeMillis().toString()
        val apiSecret = BuildConfig.MARVEL_SECRET
        val apiKey = BuildConfig.MARVEL_API
        val hash = getHash(ts, apiSecret, apiKey)

        val clientInterceptor = Interceptor { chain ->
            var request: Request = chain.request()
            var url: HttpUrl =
                request.url.newBuilder()
                    .addQueryParameter("ts", ts)
                    .addQueryParameter("apiKey", apiKey)
                    .addQueryParameter("hash", hash)
                    .build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder().addInterceptor(clientInterceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    val api: MarvelApi = getRetrofit().create(MarvelApi::class.java)
}