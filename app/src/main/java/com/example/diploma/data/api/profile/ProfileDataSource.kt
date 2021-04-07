package com.example.diploma.data.api.profile

import com.example.diploma.data.api.BaseDataSource
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.profile.UserProfile
import com.example.diploma.data.model.Result
import retrofit2.Retrofit
import javax.inject.Inject

interface ProfileDataSource {
    suspend fun getProfile(token: String): Result<UserProfile>
    suspend fun editProfile(token: String, userProfile: UserProfile): Result<BaseResponse>
}

class ProfileDataSourceImpl @Inject constructor(
    private val profileApiHelper: ProfileApiHelper,
    retrofit: Retrofit
): ProfileDataSource, BaseDataSource(retrofit) {

    override suspend fun getProfile(token: String): Result<UserProfile> {
        return getResponse(
            request = { profileApiHelper.getProfile(token) },
            defaultErrorMessage = "Error"
        )
    }

    override suspend fun editProfile(token: String, userProfile: UserProfile): Result<BaseResponse> {
        return getResponse(
                request = { profileApiHelper.editProfile(token, userProfile) },
                defaultErrorMessage = "Error"
        )
    }

}