package com.example.diploma.data.api.profile

import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.login.ui.login.User
import com.example.diploma.ui.profile.UserProfile
import com.example.diploma.ui.profileEdit.PasswordEditRequest
import retrofit2.Response
import javax.inject.Inject

interface ProfileApiHelper {

    suspend fun getProfile(token: String): Response<User>
    suspend fun editProfile(token: String, userProfile: UserProfile): Response<BaseResponse>
    suspend fun editPassword(token: String, passwordEditRequest: PasswordEditRequest): Response<BaseResponse>

}

class ProfileApiHelperImpl @Inject constructor(
    private val profileService: ProfileService
): ProfileApiHelper {

    override suspend fun getProfile(token: String): Response<User> {
        return profileService.getProfile(token)
    }

    override suspend fun editProfile(token: String, userProfile: UserProfile): Response<BaseResponse> {
        return profileService.editProfile(token, userProfile)
    }

    override suspend fun editPassword(token: String, passwordEditRequest: PasswordEditRequest): Response<BaseResponse> {
        return profileService.editPassword(token, passwordEditRequest)
    }

}