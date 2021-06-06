package com.example.diploma.ui.passwordReset.ui.emailPrompt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.diploma.R
import com.example.diploma.data.model.Result
import com.example.diploma.databinding.FragmentEmailPromptBinding
import com.example.diploma.ui.login.ui.login.afterTextChanged
import com.example.diploma.ui.passwordReset.ui.PasswordResetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailPromptFragment : Fragment() {

    private val viewModel: PasswordResetViewModel by viewModels()
    private lateinit var binding: FragmentEmailPromptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_email_prompt,
            container,
            false
        )
        binding.lifecycleOwner = this

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailEditText.afterTextChanged {
            viewModel.forgotPasswordDataChanged(it)
        }

        binding.forgotPasswordButton.setOnClickListener {
            viewModel.forgotPassword(binding.emailEditText.text.toString())
        }

        viewModel.forgotPasswordForm.observe(viewLifecycleOwner, Observer {
            val forgotPasswordFormState = it ?: return@Observer

            binding.forgotPasswordButton.isEnabled = forgotPasswordFormState.isDataValid

            if (forgotPasswordFormState.emailError != null) {
                binding.emailEditText.error = getString(forgotPasswordFormState.emailError)
            }

        })

        viewModel.forgotPasswordResult.observe(viewLifecycleOwner, Observer {
            val forgotPasswordResult = it ?: return@Observer
            when (forgotPasswordResult.status) {
                Result.Status.LOADING -> {
                    binding.emailPromptProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.emailPromptProgressbar.visibility = View.GONE
                    Toast.makeText(requireActivity(), forgotPasswordResult.data?.response, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(EmailPromptFragmentDirections.actionEmailPromptFragmentToPasswordResetFragment(
                            binding.emailEditText.text.toString()
                    ))
                }
                Result.Status.ERROR -> {
                    binding.emailPromptProgressbar.visibility = View.GONE
                    Toast.makeText(requireActivity(), forgotPasswordResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

}