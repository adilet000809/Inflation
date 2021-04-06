package com.example.diploma.ui.passwordReset.ui.reset

data class PasswordResetRequest(
    val email: String,
    val newPassword: String,
    val code: String
)
