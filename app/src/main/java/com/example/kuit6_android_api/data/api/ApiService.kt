package com.example.kuit6_android_api.data.api

import android.R.attr.value
import com.example.kuit6_android_api.data.model.request.LoginRequest
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.BaseResponse
import com.example.kuit6_android_api.data.model.response.LoginResponse
import com.example.kuit6_android_api.data.model.response.PostResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(value = "/api/posts")
    suspend fun getPosts(): BaseResponse<List<PostResponse>>

    @POST(value= "/api/posts")
    suspend fun createPost(
        @Query(value = "author") author: String = "경민",
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>

    @GET(value= "/api/posts/{id}")
    suspend fun getPostDetail(
        @Path("id") id: Long
    ): BaseResponse<PostResponse>

    @PUT(value = "/api/posts/{id}")
    suspend fun editPost(
        @Path(value= "id") id: Long,
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>

    @DELETE(value = "/api/posts/{id}")
    suspend fun deletePost(
        @Path(value = "id") id: Long,
    ) : BaseResponse<Unit>


    @POST("/api/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): BaseResponse<Map<String, String>>

    @POST("/api/auth/signup")
    suspend fun signup(
        @Body request: LoginRequest
    ): BaseResponse<LoginResponse>

    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): BaseResponse<LoginResponse>

    @GET("/api/auth/validate")
    suspend fun validateToken(): BaseResponse<Boolean>
}