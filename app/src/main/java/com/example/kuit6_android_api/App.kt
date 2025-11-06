package com.example.kuit6_android_api

import android.app.Application
import com.example.kuit6_android_api.data.di.AppContaitner

//상속 받아서 앱 실행시 단 한번만 설정된다
//사용하려면 manifests에 가서
class App : Application() {
    lateinit var contaitner: AppContaitner

    override fun onCreate() {
        super.onCreate()
        contaitner = AppContaitner()
    }
}