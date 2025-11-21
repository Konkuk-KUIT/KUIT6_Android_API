package com.example.kuit6_android_api.data.api

import android.content.Context
import com.example.kuit6_android_api.data.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context,
    private val tokenRepository: TokenRepository
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // DataStore에서 토큰 불러오기 (runBlocking 사용)
        val token = runBlocking {
            tokenRepository.getToken(context)
        }
        
        // 토큰이 있으면 Authorization 헤더에 추가
        val newRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        
        return chain.proceed(newRequest)
    }
}
