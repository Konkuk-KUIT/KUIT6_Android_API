package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kuit6_android_api.App
import okhttp3.Call

object PostViewModelFactory {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                as App

            val postRepository = application.container.postRepository

            PostListViewModel(postRepository)
        }
    }
}