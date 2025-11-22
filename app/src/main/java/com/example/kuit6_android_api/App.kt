package com.example.kuit6_android_api

import android.app.Application
import com.example.kuit6_android_api.data.api.RetrofitClient
import com.example.kuit6_android_api.data.di.AppContainer

class App: Application() {
    //Application은 Context의 하위 클래스
    lateinit var container: AppContainer

    override fun onCreate(){
        super.onCreate()
        RetrofitClient.init(this)
        //Application Context 전달
        container = AppContainer()
    }
}