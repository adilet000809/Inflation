package com.example.diploma.ui.passwordReset.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diploma.R
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.data.model.Result
import com.example.diploma.data.util.FormState
import com.example.diploma.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _forgotPasswordForm = MutableLiveData<FormState>()
    val forgotPasswordForm: LiveData<FormState> = _forgotPasswordForm

    private val _resetPasswordForm = MutableLiveData<FormState>()
    val resetPasswordForm: LiveData<FormState> = _resetPasswordForm

    private val _forgotPasswordResult = MutableLiveData<Result<BaseResponse>>()
    val forgotPasswordResult: LiveData<Result<BaseResponse>> = _forgotPasswordResult

    private val _resetPasswordResult = MutableLiveData<Result<BaseResponse>>()
    val resetPasswordResult: LiveData<Result<BaseResponse>> = _resetPasswordResult


    fun forgotPassword(email: String) {
        viewModelScope.launch {
            authRepository.forgotPassword(email).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _forgotPasswordResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _forgotPasswordResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _forgotPasswordResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun resetPassword(email: String, newPassword: String, code: String) {
        viewModelScope.launch {
            authRepository.resetPassword(email, newPassword, code).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _resetPasswordResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _resetPasswordResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _resetPasswordResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun forgotPasswordDataChanged(email: String = "") {
        if (!isEmailValid(email)) {
            _forgotPasswordForm.value = FormState(emailError = R.string.invalid_email)
        } else {
            _forgotPasswordForm.value = FormState(isDataValid = true)
        }
    }

    fun resetPasswordDataChanged(code: String, password1: String, password2: String) {
        if (!isCodeValid(code)) {
            _resetPasswordForm.value = FormState(codeError = R.string.invalid_code)
        } else if (!isPasswordValid(password1)) {
            _resetPasswordForm.value = FormState(passwordError = R.string.invalid_password)
        } else if (!isPasswordMatch(password1, password2)) {
            _resetPasswordForm.value = FormState(passwordMatchError = R.string.password_not_match)
        } else {
            _resetPasswordForm.value = FormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.trim().matches(emailPattern.toRegex())
    }

    private fun isCodeValid(code: String): Boolean {
        return code.length >= 6
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.matches(passwordValidationPattern)
    }

    private fun isPasswordMatch(password1: String, password2: String): Boolean {
        return password1 == password2
    }

    companion object {
        private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        private val passwordValidationPattern = Regex("^(?=.*[0-9]).{8,15}$")
    }

}