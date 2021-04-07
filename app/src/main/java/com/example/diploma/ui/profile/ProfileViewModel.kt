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

    private val _userProfileResult = MutableLiveData<Result<UserProfile>>()
    val userProfileResult: LiveData<Result<UserProfile>> = _userProfileResult

    private val _userProfileEditResult = MutableLiveData<Result<BaseResponse>>()
    val userProfileEditResult: LiveData<Result<BaseResponse>> = _userProfileEditResult

    fun getProfile(token: String) {
        viewModelScope.launch {
            profileRepository.getProfile("Bearer $token").collect {
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
            profileRepository.editProfile("Bearer $token", userProfile).collect {
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

    fun profileEditDataChanged(email: String, firstName: String, lastName: String) {
        if (!isEmailValid(email)) {
            _userProfileEditForm.value = FormState(emailError = R.string.invalid_email)
        } else if (firstName.isEmpty() || lastName.isEmpty()) {
            _userProfileEditForm.value = FormState(nameError = R.string.invalid_name)
        } else {
            _userProfileEditForm.value = FormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.trim().matches(emailPattern.toRegex())
    }

    companion object {
        private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    }

}