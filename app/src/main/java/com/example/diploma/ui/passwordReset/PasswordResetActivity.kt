package com.example.diploma.ui.passwordReset

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.diploma.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)
    }
}