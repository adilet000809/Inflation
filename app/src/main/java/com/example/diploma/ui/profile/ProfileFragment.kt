package com.example.diploma.ui.profile

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
import com.example.diploma.databinding.ProfileFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject lateinit var sessionManager: SessionManager
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.profile_fragment,
                container,
                false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.getProfile(sessionManager.fetchAuthToken()!!)

        profileViewModel.userProfileResult.observe(viewLifecycleOwner, Observer {
            val userProfileResult = it ?: return@Observer

            when (userProfileResult.status) {
                Result.Status.LOADING -> {
                    binding.userProfileProgressBar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.userProfileProgressBar.visibility = View.GONE
                    binding.userNameTextView.text = getString(R.string.user_profile_name,
                        userProfileResult.data?.firstName, userProfileResult.data?.lastName)
                }
                Result.Status.ERROR -> {
                    binding.userProfileProgressBar.visibility = View.GONE
                    Toast.makeText(requireActivity(), userProfileResult.message, Toast.LENGTH_SHORT).show()
                }
            }

        })

        binding.userProfileEditTextView.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToProfileChangeFragment())
        }
    }

}