package com.example.diploma.ui.login.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val userName: String?,
    val token: String?,
)