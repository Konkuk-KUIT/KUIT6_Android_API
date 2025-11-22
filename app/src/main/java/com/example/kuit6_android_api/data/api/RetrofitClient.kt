package com.example.kuit6_android_api.data.api

import android.content.Context
import com.example.kuit6_android_api.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private lateinit var context: Context
    fun init(context: Context) {
        this.context = context
    }
    // init 호출 후에야 context 변수 초기화

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor: AuthInterceptor
        get() = AuthInterceptor(context)
    //'헤더에 토큰 담아서 검증 요청 보내기'를 수행하기 위해 토큰을 담기 전 토큰이 최신화된 상태여야 함
    // 인터셉터를 새로 만들 때마다 최신 토큰을 읽게 됨
    // get(), 즉, 커스텀 게터는 해당 변수가 쓰일 때마다 get() 호출
    //AuthInterceptor(context)를 통해 새 인터셉터 객체 반환

    private val okHttpClient: OkHttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    private val json = Json {
        ignoreUnknownKeys = true  // 서버에서 추가 필드가 와도 무시
        coerceInputValues = true   // null이 와야 할 곳에 다른 값이 와도 처리
    }

    private val retrofit: Retrofit by lazy {
        //by lazy:변수를 선언할 때 바로 초기화하지 않고 변수가 처음 사용되는 시점에 초기화
        //한 번 초기화 후 실행되지 않음
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val apiService: ApiService by lazy {
        //retrofit을 lazy로 변경하면서 마찬가지로 lazy로 변경
        retrofit.create(ApiService::class.java)
    }
}