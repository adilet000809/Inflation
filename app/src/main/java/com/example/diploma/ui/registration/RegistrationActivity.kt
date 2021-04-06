package com.example.diploma.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.diploma.R
import com.example.diploma.ui.login.ui.login.afterTextChanged
import androidx.lifecycle.Observer
import com.example.diploma.data.model.Result
import com.example.diploma.ui.login.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val username = findViewById<EditText>(R.id.registration_username)
        val email = findViewById<EditText>(R.id.registration_email)
        val firstName = findViewById<EditText>(R.id.registration_first_name)
        val lastName = findViewById<EditText>(R.id.registration_last_name)
        val password1 = findViewById<EditText>(R.id.registration_password)
        val password2 = findViewById<EditText>(R.id.registration_password2)
        val registrationButton = findViewById<Button>(R.id.registration_button)
        val registrationProgressBar = findViewById<ProgressBar>(R.id.registration_progressbar)

        registrationViewModel.registrationFormState.observe(this@RegistrationActivity, Observer {
            val registrationState = it ?: return@Observer

            registrationButton.isEnabled = registrationState.isDataValid

            if (registrationState.usernameError != null) {
                username.error = getString(registrationState.usernameError)
            }
            if (registrationState.emailError != null) {
                email.error = getString(registrationState.emailError)
            }
            if (registrationState.nameError != null) {
                firstName.error = getString(registrationState.nameError)
                lastName.error = getString(registrationState.nameError)
            }
            if (registrationState.passwordError != null) {
                password1.error = getString(registrationState.passwordError)
            }
            if (registrationState.passwordMatchError != null) {
                password2.error = getString(registrationState.passwordMatchError)
            }
        })

        registrationViewModel.registrationResult.observe(this@RegistrationActivity, Observer {
            val registrationResult = it ?: return@Observer
            when (registrationResult.status) {
                Result.Status.LOADING -> {
                    registrationProgressBar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    registrationProgressBar.visibility = View.GONE
                    Toast.makeText(this, registrationResult.data?.response, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                Result.Status.ERROR -> {
                    registrationProgressBar.visibility = View.GONE
                    Toast.makeText(this, registrationResult.message, Toast.LENGTH_SHORT).show()
                }
            }

        })

        username.afterTextChanged {
            registrationViewModel.registrationDataChanged(
                    username.text.toString(),
                    email.text.toString(),
                    firstName.text.toString(),
                    lastName.text.toString(),
                    password1.text.toString(),
                    password2.text.toString()
            )
        }

        email.afterTextChanged {
            registrationViewModel.registrationDataChanged(
                    username.text.toString(),
                    email.text.toString(),
                    firstName.text.toString(),
                    lastName.text.toString(),
                    password1.text.toString(),
                    password2.text.toString()
            )
        }

        firstName.afterTextChanged {
            registrationViewModel.registrationDataChanged(
                    username.text.toString(),
                    email.text.toString(),
                    firstName.text.toString(),
                    lastName.text.toString(),
                    password1.text.toString(),
                    password2.text.toString()
            )
        }

        lastName.afterTextChanged {
            registrationViewModel.registrationDataChanged(
                    username.text.toString(),
                    email.text.toString(),
                    firstName.text.toString(),
                    lastName.text.toString(),
                    password1.text.toString(),
                    password2.text.toString()
            )
        }

        password1.afterTextChanged {
            registrationViewModel.registrationDataChanged(
                    username.text.toString(),
                    email.text.toString(),
                    firstName.text.toString(),
                    lastName.text.toString(),
                    password1.text.toString(),
                    password2.text.toString()
            )
        }

        password2.afterTextChanged {
            registrationViewModel.registrationDataChanged(
                    username.text.toString(),
                    email.text.toString(),
                    firstName.text.toString(),
                    lastName.text.toString(),
                    password1.text.toString(),
                    password2.text.toString()
            )
        }

        registrationButton.setOnClickListener {
            registrationProgressBar.visibility = View.VISIBLE
            registrationViewModel.register(
                    username.text.toString(),
                    email.text.toString(),
                    firstName.text.toString(),
                    lastName.text.toString(),
                    password2.text.toString()
            )
        }

    }

}