package com.example.diploma.data.repository

import com.example.diploma.data.api.auth.AuthDataSource
import com.example.diploma.data.model.Result
import com.example.diploma.ui.login.ui.login.LoginRequest
import com.example.diploma.ui.login.ui.login.LoginResult
import com.example.diploma.ui.registration.RegistrationRequest
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.passwordReset.ui.emailPrompt.ForgotPasswordRequest
import com.example.diploma.ui.passwordReset.ui.reset.PasswordResetRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
) {

    suspend fun login(username: String, password: String): Flow<Result<LoginResult>> {
        return flow {
            emit(Result.loading())
            val loginResult = authDataSource.login(LoginRequest(username, password))
            emit(loginResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun register(username: String, email: String, firstName: String, lastName: String, password: String): Flow<Result<BaseResponse>> {
        return flow {
            emit(Result.loading())
            val loginResult = authDataSource.register(RegistrationRequest(
                firstName, lastName, username, email, password
            ))
            emit(loginResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun forgotPassword(email: String): Flow<Result<BaseResponse>> {
        return flow {
            emit(Result.loading())
            val forgotPasswordResult = authDataSource.forgotPassword(ForgotPasswordRequest(email))
            emit(forgotPasswordResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun resetPassword(email: String, newPassword: String, code: String): Flow<Result<BaseResponse>> {
        return flow {
            emit(Result.loading())
            val resetPasswordResult = authDataSource.resetPassword(PasswordResetRequest(email, newPassword, code))
            emit(resetPasswordResult)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun logout() {
        authDataSource.logout()
    }

}