package com.example.diploma.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.data.model.Result
import com.example.diploma.databinding.FragmentPasswordEditBinding
import com.example.diploma.ui.login.ui.login.afterTextChanged
import com.example.diploma.ui.profile.ProfileViewModel
import com.example.diploma.ui.profileEdit.PasswordEditRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PasswordEditFragment : Fragment() {

    @Inject lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentPasswordEditBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_password_edit,
                container,
                false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        profileViewModel.passwordEditForm.observe(requireActivity(), Observer {
            val passwordEditState = it ?: return@Observer

            binding.editPasswordSaveButton.isEnabled = passwordEditState.isDataValid

            if (passwordEditState.currentPasswordError != null) {
                binding.currentPassword.error = getString(passwordEditState.currentPasswordError)
            }
            if (passwordEditState.passwordError != null) {
                binding.editPassword1.error = getString(passwordEditState.passwordError)
            }
            if (passwordEditState.passwordMatchError != null) {
                binding.editPassword2.error = getString(passwordEditState.passwordMatchError)
            }

        })

        binding.currentPassword.afterTextChanged {
            profileViewModel.passwordEditDataChanged(
                it,
                binding.editPassword1.text.toString(),
                binding.editPassword2.text.toString()

            )
        }

        binding.editPassword1.afterTextChanged {
            profileViewModel.profileEditDataChanged(
                binding.currentPassword.text.toString(),
                it,
                binding.editPassword2.text.toString()

            )
        }

        binding.editPassword2.afterTextChanged {
            profileViewModel.profileEditDataChanged(
                binding.currentPassword.text.toString(),
                binding.editPassword1.text.toString(),
                it

            )
        }

        profileViewModel.passwordEditResult.observe(requireActivity(), Observer {
            val passwordEditResult = it ?: return@Observer

            when (passwordEditResult.status) {
                Result.Status.LOADING -> {
                    binding.editPasswordProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.editPasswordProgressbar.visibility = View.GONE
                    sessionManager.saveAuthToken(passwordEditResult.data?.response!!)
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                }
                Result.Status.ERROR -> {
                    binding.editPasswordProgressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), passwordEditResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.editPasswordSaveButton.setOnClickListener {
            profileViewModel.editPassword(
                sessionManager.fetchAuthToken()!!,
                PasswordEditRequest(
                    binding.currentPassword.text.toString(),
                    binding.editPassword2.text.toString()
                )
            )
        }

    }

}