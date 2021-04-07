package com.example.diploma.data.api.profile

import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.profile.UserProfile
import retrofit2.Response
import javax.inject.Inject

interface ProfileApiHelper {

    suspend fun getProfile(token: String): Response<UserProfile>
    suspend fun editProfile(token: String, userProfile: UserProfile): Response<BaseResponse>

}

class ProfileApiHelperImpl @Inject constructor(
    private val profileService: ProfileService
): ProfileApiHelper {

    override suspend fun getProfile(token: String): Response<UserProfile> {
        return profileService.getProfile(token)
    }

    override suspend fun editProfile(token: String, userProfile: UserProfile): Response<BaseResponse> {
        return profileService.editProfile(token, userProfile)
    }

}