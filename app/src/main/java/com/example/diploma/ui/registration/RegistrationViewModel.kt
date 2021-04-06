package com.example.diploma.ui.registration

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
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registrationForm = MutableLiveData<FormState>()
    val registrationFormState: LiveData<FormState> = _registrationForm

    private val _registrationResult = MutableLiveData<Result<BaseResponse>>()
    val registrationResult: LiveData<Result<BaseResponse>> = _registrationResult

    fun register(username: String, email: String, firstName: String, lastName: String, password: String) {
        viewModelScope.launch {
            authRepository.register(username, email, firstName, lastName, password).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _registrationResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _registrationResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _registrationResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun registrationDataChanged(username: String, email: String, firstName: String, lastName: String, password1: String, password2: String) {
        if (username.isEmpty()) {
            _registrationForm.value = FormState(usernameError = R.string.invalid_username)
        } else if (!isEmailValid(email)) {
            _registrationForm.value = FormState(emailError = R.string.invalid_email)
        } else if (firstName.isEmpty() || lastName.isEmpty()) {
            _registrationForm.value = FormState(nameError = R.string.invalid_name)
        } else if (!isPasswordValid(password1)) {
            _registrationForm.value = FormState(passwordError = R.string.invalid_password)
        } else if (!isPasswordMatch(password1, password2)) {
            _registrationForm.value = FormState(passwordMatchError = R.string.password_not_match)
        } else {
            _registrationForm.value = FormState(isDataValid = true)
        }
    }

    private fun isNameValid(name: String): Boolean {
        return name.isEmpty()
    }

    private fun isEmailValid(email: String): Boolean {
        return email.trim().matches(emailPattern.toRegex())
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.isNotEmpty()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    private fun isPasswordMatch(password1: String, password2: String): Boolean {
        return password1 == password2
    }

    companion object {
        private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    }
}