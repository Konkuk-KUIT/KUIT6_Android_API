package com.example.kuit6_android_api.data.api

import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.BaseResponse
import com.example.kuit6_android_api.data.model.response.PostResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

//file 들어가서 project structure 에서 retrofit2 추가 해야된
interface ApiService {
    @GET(value = "/api/posts")
    suspend fun getPosts(): BaseResponse<List<PostResponse>>

    @POST("/api/posts")
    suspend fun createPost(
        @Query("author") author: String = "규빈",
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>//반환 값

    @GET(value = "/api/posts/{id}")
    suspend fun getPostDetail(
        @Path("id") id: Long
    ): BaseResponse<PostResponse>

    @DELETE("/api/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Long
    ): BaseResponse<PostResponse>

    @PUT("/api/posts/{id}")
    suspend fun editPost(
        @Path("id") id: Long,
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>
}