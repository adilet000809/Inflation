package com.example.diploma.ui.login.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diploma.R
import com.example.diploma.data.repository.AuthRepository
import com.example.diploma.data.model.Result
import com.example.diploma.data.util.FormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginForm = MutableLiveData<FormState>()
    val loginFormState: LiveData<FormState> = _loginForm

    private val _loginResult = MutableLiveData<Result<LoginResult>>()
    val loginResult: LiveData<Result<LoginResult>> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            authRepository.login(username, password).collect {
                if (it.status == Result.Status.SUCCESS) {
                    _loginResult.value = Result.success(it.data)
                }
                if (it.status == Result.Status.LOADING) {
                    _loginResult.value = Result.loading()
                }
                if (it.status == Result.Status.ERROR) {
                    _loginResult.value = Result.error(it.message ?: "Error", it.error)
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = FormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = FormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = FormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.length >= 5
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }
}