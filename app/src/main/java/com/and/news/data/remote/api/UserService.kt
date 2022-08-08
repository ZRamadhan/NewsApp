package com.and.news.data.remote.api

import com.and.news.data.remote.model.Data
import com.and.news.data.remote.model.SignInRequest
import com.and.news.data.remote.model.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @POST("auth/register")
    fun registerUser(@Body signUpRequest: SignUpRequest): NetworkCall<Data>

    @POST("auth/login")
    fun loginUser(@Body signInRequest: SignInRequest): NetworkCall<Data>

    @GET("users")
    fun getUser(): NetworkCall<Data>
}