package com.example.kuit6_android_api

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
//    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        instance = this
//        container = AppContainer()
    }

    companion object {
        lateinit var instance: App   // 전역 Application
            private set
    }
}