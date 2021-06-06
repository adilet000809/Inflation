package com.example.diploma.ui.login.ui.login

data class LoginResult(
    val user: User?,
    val token: String?,
)

data class User(
    val id: Int?,
    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val email: String?,
    val total: Double?,
    val lastUpdate: Long = System.currentTimeMillis()
)
