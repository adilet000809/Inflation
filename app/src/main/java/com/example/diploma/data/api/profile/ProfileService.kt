package com.example.diploma.data.api.profile

import com.example.diploma.ui.profile.UserProfile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileService {

    @GET("user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserProfile>

}