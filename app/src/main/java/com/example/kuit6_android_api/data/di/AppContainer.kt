package com.example.kuit6_android_api.data.di

import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.api.RetrofitClient
import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.data.repository.LoginRepositoryImpl
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.data.repository.PostRepositoryImpl
import com.example.kuit6_android_api.data.repository.TokenRepository
import com.example.kuit6_android_api.data.repository.TokenRepositoryImpl

class AppContainer { //모든 의존성을 AppContainer 한 곳에서 관리하게 함
    private val apiService: ApiService by lazy{
        RetrofitClient.apiService
    }

    val postRepository: PostRepository by lazy{
    //ApiService를 AppContainer에서 한 번만 가져 와 Repository에 주입

    // 원래:뷰모델 -> ApiService 직접 참조
    // 현재:뷰모델 -> Repository -> ApiService Repository 거쳐 참조
        // 파라미터로 전달 : 의존성 수동 주입
        PostRepositoryImpl(apiService)
        //자동:라이브러리가 있다 다음 주차
    }

    val loginRepository: LoginRepository by lazy{
        LoginRepositoryImpl(apiService)
    }

    val tokenRepository: TokenRepository by lazy{
        TokenRepositoryImpl()
    }
}