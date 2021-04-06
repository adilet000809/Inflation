package com.example.diploma.ui.profileEdit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.diploma.R
import com.example.diploma.ui.profile.ProfileViewModel

class ProfileEditFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_edit_fragment, container, false)
    }

}