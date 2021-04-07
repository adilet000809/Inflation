package com.example.diploma.data.repository

import com.example.diploma.data.api.profile.ProfileDataSource
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.data.model.Result
import com.example.diploma.ui.profile.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileDataSource: ProfileDataSource
) {

    suspend fun getProfile(token: String): Flow<Result<UserProfile>> {
        return flow {
            emit(Result.loading())
            val loginResult = profileDataSource.getProfile(token)
            emit(loginResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun editProfile(token: String, userProfile: UserProfile): Flow<Result<BaseResponse>> {
        return flow {
            emit(Result.loading())
            val profileEditResult = profileDataSource.editProfile(token, userProfile)
            emit(profileEditResult)
        }.flowOn(Dispatchers.IO)
    }
}