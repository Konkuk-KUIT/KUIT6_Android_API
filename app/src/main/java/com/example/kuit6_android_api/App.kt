package com.example.kuit6_android_api

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
//앱 컨테이너 = 수동 주입은 지워도 된다.
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
        private set
    }
}