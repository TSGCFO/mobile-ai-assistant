package com.HassanSadiq.AIChatAssistant.network

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @POST("data")
    suspend fun createUser(@Body request: CreateUserRequest): Response<UserResponse>
    
    @FormUrlEncoded
    @POST("aiapi/answertext")
    suspend fun getAIResponse(
        @Field("app_id") appId: String,
        @Field("query") query: String
    ): Response<AIResponse>
}