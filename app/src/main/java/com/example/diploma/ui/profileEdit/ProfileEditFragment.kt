package com.example.diploma.ui.profileEdit

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
import com.example.diploma.databinding.ProfileEditFragmentBinding
import com.example.diploma.ui.login.ui.login.afterTextChanged
import com.example.diploma.ui.profile.ProfileViewModel
import com.example.diploma.ui.profile.UserProfile
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {

    @Inject lateinit var sessionManager: SessionManager
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var binding: ProfileEditFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.profile_edit_fragment,
                container,
                false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileEditFirstName.setText(profileViewModel.userProfileResult.value?.data?.firstName)
        binding.profileEditLastName.setText(profileViewModel.userProfileResult.value?.data?.lastName)
        binding.profileEditEmail.setText(profileViewModel.userProfileResult.value?.data?.email)

        profileViewModel.userProfileEditForm.observe(requireActivity(), Observer {
            val profileEditState = it ?: return@Observer

            binding.profileEditSaveButton.isEnabled = profileEditState.isDataValid

            if (profileEditState.emailError != null) {
                binding.profileEditEmail.error = getString(profileEditState.emailError)
            }
            if (profileEditState.nameError != null) {
                binding.profileEditFirstName.error = getString(profileEditState.nameError)
                binding.profileEditLastName.error = getString(profileEditState.nameError)
            }
        })

        profileViewModel.userProfileEditResult.observe(requireActivity(), Observer {
            val profileEditResult = it ?: return@Observer

            when (profileEditResult.status) {
                Result.Status.LOADING -> {
                    binding.profileEditProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.profileEditProgressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), profileEditResult.data?.response, Toast.LENGTH_SHORT).show()
                }
                Result.Status.ERROR -> {
                    binding.profileEditProgressbar.visibility = View.GONE
                    val errorMessage = profileEditResult.message
                    if (errorMessage.equals("Email")) {
                        binding.profileEditEmail.error = getString(R.string.email_exists)
                    }
                }
            }
        })

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.profileEditFirstName.afterTextChanged {
            profileViewModel.profileEditDataChanged(
                    binding.profileEditEmail.text.toString(),
                    it,
                    binding.profileEditLastName.text.toString()
            )
        }

        binding.profileEditLastName.afterTextChanged {
            profileViewModel.profileEditDataChanged(
                    binding.profileEditEmail.text.toString(),
                    binding.profileEditFirstName.text.toString(),
                    it
            )
        }

        binding.profileEditEmail.afterTextChanged {
            profileViewModel.profileEditDataChanged(
                    it,
                    binding.profileEditFirstName.text.toString(),
                    binding.profileEditLastName.text.toString()
            )
        }

        binding.profileEditSaveButton.setOnClickListener {
            profileViewModel.editProfile(sessionManager.fetchAuthToken()!!, UserProfile(
                    firstName = binding.profileEditFirstName.text.toString(),
                    lastName = binding.profileEditLastName.text.toString(),
                    email = binding.profileEditEmail.text.toString()
            ))
        }
    }
}
