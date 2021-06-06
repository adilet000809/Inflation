package com.example.diploma.data.repository

import com.example.diploma.data.api.profile.ProfileDataSource
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.data.model.Result
import com.example.diploma.ui.login.ui.login.User
import com.example.diploma.ui.profile.UserProfile
import com.example.diploma.ui.profileEdit.PasswordEditRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileDataSource: ProfileDataSource,
) {

    suspend fun getProfile(token: String): Flow<Result<User>> {
        return flow {
            emit(Result.loading())
            val profileResult = profileDataSource.getProfile(token)
            emit(profileResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun editProfile(token: String, userProfile: UserProfile): Flow<Result<BaseResponse>> {
        return flow {
            emit(Result.loading())
            val profileEditResult = profileDataSource.editProfile(token, userProfile)
            emit(profileEditResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun editPassword(token: String, passwordEditRequest: PasswordEditRequest): Flow<Result<BaseResponse>> {
        return flow {
            emit(Result.loading())
            val passwordEditResult = profileDataSource.editPassword(token, passwordEditRequest)
            emit(passwordEditResult)
        }.flowOn(Dispatchers.IO)
    }
}
