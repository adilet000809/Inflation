package com.example.diploma.data.api.auth

import com.example.diploma.ui.login.ui.login.LoginRequest
import com.example.diploma.ui.login.ui.login.LoginResult
import com.example.diploma.ui.passwordReset.ui.emailPrompt.ForgotPasswordRequest
import com.example.diploma.ui.registration.RegistrationRequest
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.ui.passwordReset.ui.reset.PasswordResetRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResult>

    @POST("auth/register")
    suspend fun register(@Body registrationRequest: RegistrationRequest): Response<BaseResponse>

    @POST("auth/forgot")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Response<BaseResponse>

    @POST("auth/reset")
    suspend fun resetPassword(@Body passwordResetRequest: PasswordResetRequest): Response<BaseResponse>

}