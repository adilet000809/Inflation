package com.example.diploma.ui.profile

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
import androidx.navigation.fragment.findNavController
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.data.model.Result
import com.example.diploma.databinding.FragmentProfileBinding
import com.example.diploma.ui.splash.SplashScreenActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject lateinit var sessionManager: SessionManager
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_profile,
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
                    binding.profileFragmentUserProfileProgressBar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.profileFragmentUserProfileProgressBar.visibility = View.GONE
                    binding.profileFragmentUserNameTextView.text = getString(R.string.user_profile_name,
                        userProfileResult.data?.firstName, userProfileResult.data?.lastName)
                    binding.profileFragmentUserTotalExpenditureTextView.text = userProfileResult.data?.total.toString()
                }
                Result.Status.ERROR -> {
                    binding.profileFragmentUserProfileProgressBar.visibility = View.GONE
                    Toast.makeText(requireActivity(), userProfileResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.profileFragmentUserProfileEditTextView.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToProfileChangeFragment())
        }
        binding.profileFragmentUserPasswordChangeTextView.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPasswordEditFragment())
        }
        binding.profileFragmentUserPurchaseListTextView.setOnClickListener {
            findNavController().navigate(R.id.purchaseListFragment)
        }
        binding.profileFragmentUserPurchasesTextView.setOnClickListener {
            findNavController().navigate(R.id.purchaseFragment)
        }
        binding.profileFragmentLogoutImageView.setOnClickListener {
            sessionManager.deleteAuthToken()
            startActivity(Intent(requireContext(), SplashScreenActivity::class.java))
        }
    }
}