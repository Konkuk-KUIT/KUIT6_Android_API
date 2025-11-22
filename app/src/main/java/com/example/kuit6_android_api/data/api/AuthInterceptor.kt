package com.example.kuit6_android_api.data.api

import android.content.Context
import com.example.kuit6_android_api.data.repository.TokenRepositoryImpl
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context): Interceptor {
    //발생한 요청을 가로채 수정
    //Interceptor에서 DataStore 접근을 하기 위해 Context 파라미터 필요
    private val tokenRepository = TokenRepositoryImpl()
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            //블록이 완료될 때까지 intercept를 호출한 스레드의 실행 멈춤
            tokenRepository.getToken(context)
        }
        
        val requestBuilder = chain.request().newBuilder()
        //request에 헤더를 추가하기 위해 빌더 생성
        //request:주요한 요청 header:request의 세부사항
        
        token?.let {
            //token 변수에 값이 존재할 때
            requestBuilder.addHeader("Authorization", "Bearer $it")
            // 서버로 보내는 요청마다 헤더를 추가
            // 헤더는 "Authorization"라는 이름과 토큰 $it의 쌍으로 이루어짐
            //Bearer:인증 방식을 나타내는 일종의 접두사
            //Authorization:HTTP 통신 규약에 따라 서버와 클라이언트 간 정보를 주고 받을 때 사용하는 헤더 중 하나
        }
        
        return chain.proceed(requestBuilder.build())
    }
}