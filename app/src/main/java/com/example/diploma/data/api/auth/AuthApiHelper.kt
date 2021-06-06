package com.example.diploma.data.api.auth

import com.example.diploma.ui.login.ui.login.LoginRequest
import com.example.diploma.ui.login.ui.login.LoginResult
import com.example.diploma.ui.registration.RegistrationRequest
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.passwordReset.ui.emailPrompt.ForgotPasswordRequest
import com.example.diploma.ui.passwordReset.ui.reset.PasswordResetRequest
import retrofit2.Response
import javax.inject.Inject

interface AuthApiHelper {
    suspend fun login(loginRequest: LoginRequest): Response<LoginResult>
    suspend fun register(registrationRequest: RegistrationRequest): Response<BaseResponse>
    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Response<BaseResponse>
    suspend fun resetPassword(passwordResetRequest: PasswordResetRequest): Response<BaseResponse>
}

class AuthApiHelperImpl @Inject constructor(
    private val authService: AuthService
): AuthApiHelper {

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResult> {
        return authService.login(loginRequest)
    }

    override suspend fun register(registrationRequest: RegistrationRequest): Response<BaseResponse> {
        return authService.register(registrationRequest)
    }

    override suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Response<BaseResponse> {
        return authService.forgotPassword(forgotPasswordRequest)
    }

    override suspend fun resetPassword(passwordResetRequest: PasswordResetRequest): Response<BaseResponse> {
        return authService.resetPassword(passwordResetRequest)
    }

}