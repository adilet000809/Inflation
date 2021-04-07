package com.example.diploma.ui.profile


data class UserProfile(
    val firstName: String,
    val lastName: String,
    val userName: String = "",
    val email: String,
    val total: Double = 0.0
)