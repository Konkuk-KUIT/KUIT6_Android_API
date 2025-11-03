package com.example.kuit6_android_api.data.api

import com.example.kuit6_android_api.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    //로그캣을 사용해서 어떤 통신이 오고가는지 확인 가능
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl = BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(factory = GsonConverterFactory.create())
        .build()


    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
