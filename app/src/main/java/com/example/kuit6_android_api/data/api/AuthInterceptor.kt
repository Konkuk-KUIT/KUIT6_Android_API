package com.example.kuit6_android_api.data.api

import android.content.Context
import com.example.kuit6_android_api.data.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton //inject 를 달아줘라.
class AuthInterceptor @Inject constructor(
    //context 제거
    private val tokenRepository: TokenRepository
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        // DataStore에서 토큰 불러오기 (runBlocking 사용)
        val token = runBlocking {
            tokenRepository.getToken()
        }

        val request = chain.request().newBuilder()
        if(!token.isNullOrEmpty()){
            request.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}
