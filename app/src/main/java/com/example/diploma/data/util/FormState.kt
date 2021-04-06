package com.example.diploma.data.util

data class FormState(
    val nameError: Int? = null,
    val emailError: Int? = null,
    val usernameError: Int? = null,
    val passwordMatchError: Int? = null,
    val passwordError: Int? = null,
    val codeError: Int? = null,
    val isDataValid: Boolean = false
)
