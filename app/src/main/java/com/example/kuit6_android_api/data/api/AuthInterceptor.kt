package com.example.kuit6_android_api.data.api

import android.content.Context
import com.example.kuit6_android_api.data.repository.TokenRepository
import com.example.kuit6_android_api.data.repository.TokenRepositoryImpl
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository // Hilt가 TokenRepositoryImpl을 생성해서 주입
) : Interceptor {
    //발생한 요청을 가로채 수정

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            //블록이 완료될 때까지 intercept를 호출한 스레드의 실행 멈춤
            tokenRepository.getToken()
        }

        val request = chain.request().newBuilder()
        if(!token.isNullOrEmpty()){
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}