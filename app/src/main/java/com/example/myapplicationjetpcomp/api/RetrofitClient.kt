package com.example.myapplicationjetpcomp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    private val BASE_URL = "https://fitlife-1hyp.onrender.com/api/"

    public fun getClient(): Retrofit{
        val interceptor=HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}