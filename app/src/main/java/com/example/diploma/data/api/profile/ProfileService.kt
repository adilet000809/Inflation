package com.example.diploma.data.api.profile

import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.login.ui.login.User
import com.example.diploma.ui.profile.UserProfile
import com.example.diploma.ui.profileEdit.PasswordEditRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ProfileService {

    @GET("user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<User>

    @POST("user/profile/edit")
    suspend fun editProfile(@Header("Authorization") token: String, @Body userProfile: UserProfile): Response<BaseResponse>

    @POST("user/profile/password/edit")
    suspend fun editPassword(@Header("Authorization") token: String, @Body passwordEditRequest: PasswordEditRequest): Response<BaseResponse>

}