package com.example.kuit6_android_api

import android.app.Application
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: Application() {
    //Application은 Context의 하위 클래스

    override fun onCreate(){
        super.onCreate()
        instance = this
        //Application Context 전달
    }

    companion object{
        lateinit var instance: App
            private set
    }
}