package com.example.diploma.data.api.profile

import com.example.diploma.ui.profile.UserProfile
import retrofit2.Response
import javax.inject.Inject

interface ProfileApiHelper {

    suspend fun getProfile(token: String): Response<UserProfile>

}

class ProfileApiHelperImpl @Inject constructor(
    private val profileService: ProfileService
): ProfileApiHelper {

    override suspend fun getProfile(token: String): Response<UserProfile> {
        return profileService.getProfile(token)
    }

}