package com.example.diploma.ui.profileEdit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.diploma.R
import com.example.diploma.databinding.ProfileEditFragmentBinding
import com.example.diploma.databinding.ProfileFragmentBinding
import com.example.diploma.ui.profile.ProfileViewModel

class ProfileEditFragment : Fragment() {

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

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val items = listOf("Option 1", "Option 2", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3", "Option 3")
        val adapter = ArrayAdapter(requireContext(), R.layout.city_popup_window_item, items)
        binding.profileEditCity.setAdapter(adapter)
        binding.profileEditCity.setOnItemClickListener  { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->

            Toast.makeText(requireContext(), "$position $id", Toast.LENGTH_SHORT).show()

        }

    }
}