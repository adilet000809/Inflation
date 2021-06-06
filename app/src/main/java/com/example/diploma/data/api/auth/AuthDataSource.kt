package com.example.diploma.data.api.auth

import com.example.diploma.data.api.BaseDataSource
import com.example.diploma.ui.login.ui.login.LoginRequest
import com.example.diploma.data.model.Result
import com.example.diploma.data.util.ErrorUtil
import com.example.diploma.ui.login.ui.login.LoginResult
import com.example.diploma.ui.registration.RegistrationRequest
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.passwordReset.ui.emailPrompt.ForgotPasswordRequest
import com.example.diploma.ui.passwordReset.ui.reset.PasswordResetRequest
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

interface AuthDataSource {
    suspend fun login(loginRequest: LoginRequest): Result<LoginResult>
    suspend fun register(registrationRequest: RegistrationRequest): Result<BaseResponse>
    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Result<BaseResponse>
    suspend fun resetPassword(passwordResetRequest: PasswordResetRequest): Result<BaseResponse>
    suspend fun logout()
}

class AuthDataSourceImpl @Inject constructor(
        private val authApiHelper: AuthApiHelper,
        retrofit: Retrofit
): AuthDataSource, BaseDataSource(retrofit) {

    override suspend fun login(loginRequest: LoginRequest): Result<LoginResult> {
        return getResponse(
            request = { authApiHelper.login(loginRequest) },
            defaultErrorMessage = "Error"
        )
    }

    override suspend fun register(registrationRequest: RegistrationRequest): Result<BaseResponse> {
        return getResponse(
            request = { authApiHelper.register(registrationRequest) },
            defaultErrorMessage = "Error"
        )
    }

    override suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Result<BaseResponse> {
        return getResponse(
            request = { authApiHelper.forgotPassword(forgotPasswordRequest) },
            defaultErrorMessage = "Error"
        )
    }

    override suspend fun resetPassword(passwordResetRequest: PasswordResetRequest): Result<BaseResponse> {
        return getResponse(
                request = { authApiHelper.resetPassword(passwordResetRequest) },
                defaultErrorMessage = "Error"
        )
    }

    override suspend fun logout() {

    }


}