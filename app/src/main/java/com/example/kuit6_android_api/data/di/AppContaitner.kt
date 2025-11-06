package com.example.kuit6_android_api.data.di

import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.api.RetrofitClient
import com.example.kuit6_android_api.data.model.repository.PostRepository
import com.example.kuit6_android_api.data.model.repository.PostRepositoryImpl

class AppContaitner {
    //의존성 주입
    private val apiService : ApiService by lazy{
        RetrofitClient.apiService
    }

    val postRepository: PostRepository by lazy {
        PostRepositoryImpl(apiService)
    }
}