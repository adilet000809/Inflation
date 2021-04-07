package com.example.diploma.data.api.profile

import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.profile.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ProfileService {

    @GET("user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserProfile>

    @POST("user/profile/edit")
    suspend fun editProfile(@Header("Authorization") token: String, @Body userProfile: UserProfile): Response<BaseResponse>

}