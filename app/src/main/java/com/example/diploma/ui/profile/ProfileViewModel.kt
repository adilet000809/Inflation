package com.example.diploma.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diploma.R
import com.example.diploma.data.model.BaseResponse
import com.example.diploma.data.model.Result
import com.example.diploma.data.repository.ProfileRepository
import com.example.diploma.data.util.FormState
import com.example.diploma.ui.login.ui.login.User
import com.example.diploma.ui.profileEdit.PasswordEditRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _userProfileEditForm = MutableLiveData<FormState>()
    val userProfileEditForm: LiveData<FormState> = _userProfileEditForm

    private val _passwordEditForm = MutableLiveData<FormState>()
    val passwordEditForm: LiveData<FormState> = _passwordEditForm

    private val _userProfileResult = MutableLiveData<Result<User>>()
    val userProfileResult: LiveData<Result<User>> = _userProfileResult

    private val _userProfileEditResult = MutableLiveData<Result<BaseResponse>>()
    val userProfileEditResult: LiveData<Result<BaseResponse>> = _userProfileEditResult

    private val _passwordEditResult = MutableLiveData<Result<BaseResponse>>()
    val passwordEditResult: LiveData<Result<BaseResponse>> = _passwordEditResult

    fun getProfile(token: String) {
        viewModelScope.launch {
            profileRepository.getProfile(token).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _userProfileResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _userProfileResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _userProfileResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun editProfile(token: String, userProfile: UserProfile) {
        viewModelScope.launch {
            profileRepository.editProfile(token, userProfile).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _userProfileEditResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _userProfileEditResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _userProfileEditResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun editPassword(token: String, passwordEditRequest: PasswordEditRequest) {
        viewModelScope.launch {
            profileRepository.editPassword(token, passwordEditRequest).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _passwordEditResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _passwordEditResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _passwordEditResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun profileEditDataChanged(email: String, firstName: String, lastName: String) {
        if (!isEmailValid(email)) {
            _userProfileEditForm.value = FormState(emailError = R.string.invalid_email)
        } else if (firstName.isEmpty() || lastName.isEmpty()) {
            _userProfileEditForm.value = FormState(nameError = R.string.invalid_name)
        } else {
            _userProfileEditForm.value = FormState(isDataValid = true)
        }
    }

    fun passwordEditDataChanged(currentPassword: String, password1: String, password2: String) {
        if (!isPasswordValid(currentPassword)) {
            _passwordEditForm.value = FormState(currentPasswordError = R.string.invalid_password)
        } else if (!isPasswordValid(password1)) {
            _passwordEditForm.value = FormState(passwordError = R.string.invalid_password)
        } else if (!isPasswordMatch(password1, password2)) {
            _passwordEditForm.value = FormState(passwordMatchError = R.string.password_not_match)
        } else {
            _passwordEditForm.value = FormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.trim().matches(emailPattern.toRegex())
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