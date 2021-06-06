package com.example.diploma.ui.passwordReset.ui.reset

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.diploma.R
import com.example.diploma.data.model.Result
import com.example.diploma.databinding.FragmentPasswordResetBinding
import com.example.diploma.ui.login.ui.login.LoginActivity
import com.example.diploma.ui.login.ui.login.afterTextChanged
import com.example.diploma.ui.passwordReset.ui.PasswordResetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetFragment : Fragment() {

    private val passwordResetViewModel: PasswordResetViewModel by activityViewModels()
    private val args: PasswordResetFragmentArgs by navArgs()
    private lateinit var binding: FragmentPasswordResetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_password_reset,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.passwordResetCodeEditText.afterTextChanged {
            passwordResetViewModel.resetPasswordDataChanged(
                    it,
                    binding.passwordResetPassword1.text.toString(),
                    binding.passwordResetPassword2.text.toString()
            )
        }

        binding.passwordResetPassword1.afterTextChanged {
            passwordResetViewModel.resetPasswordDataChanged(
                    binding.passwordResetCodeEditText.text.toString(),
                    it,
                    binding.passwordResetPassword2.text.toString()
            )
        }

        binding.passwordResetPassword2.afterTextChanged {
            passwordResetViewModel.resetPasswordDataChanged(
                    binding.passwordResetCodeEditText.text.toString(),
                    binding.passwordResetPassword1.text.toString(),
                    it
            )
        }

        binding.resetPasswordButton.setOnClickListener {
            passwordResetViewModel.resetPassword(
                    args.email,
                    binding.passwordResetPassword2.text.toString(),
                    binding.passwordResetCodeEditText.text.toString()
            )
        }

        binding.resetPasswordResendCodeTextView.setOnClickListener {
            passwordResetViewModel.forgotPassword(args.email)
        }

        passwordResetViewModel.resetPasswordForm.observe(viewLifecycleOwner, Observer {
            val resetPasswordFormState = it ?: return@Observer

            binding.resetPasswordButton.isEnabled = resetPasswordFormState.isDataValid
            if (resetPasswordFormState.codeError != null) {
                binding.passwordResetCodeEditText.error = getString(resetPasswordFormState.codeError)
            }
            if (resetPasswordFormState.passwordError != null) {
                binding.passwordResetPassword1.error = getString(resetPasswordFormState.passwordError)
            }
            if (resetPasswordFormState.passwordMatchError != null) {
                binding.passwordResetPassword2.error = getString(resetPasswordFormState.passwordMatchError)
            }

        })

        passwordResetViewModel.forgotPasswordResult.observe(viewLifecycleOwner, Observer {
            val forgotPasswordResult = it ?: return@Observer

            when (forgotPasswordResult.status) {
                Result.Status.LOADING -> {
                    binding.passwordResetProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.passwordResetProgressbar.visibility = View.GONE
                    Toast.makeText(requireActivity(), forgotPasswordResult.data?.response, Toast.LENGTH_SHORT).show()
                }
                Result.Status.ERROR -> {
                    binding.passwordResetProgressbar.visibility = View.GONE
                    Toast.makeText(requireActivity(), forgotPasswordResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        passwordResetViewModel.resetPasswordResult.observe(viewLifecycleOwner, Observer {
            val resetPasswordResult = it ?: return@Observer

            when (resetPasswordResult.status) {
                Result.Status.LOADING -> {
                    binding.passwordResetProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.passwordResetProgressbar.visibility = View.GONE
                    Toast.makeText(requireActivity(), resetPasswordResult.data?.response, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }
                Result.Status.ERROR -> {
                    binding.passwordResetProgressbar.visibility = View.GONE
                    Toast.makeText(requireActivity(), resetPasswordResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
