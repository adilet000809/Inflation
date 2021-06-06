package com.example.diploma.ui.home

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.data.model.Result
import com.example.diploma.databinding.FragmentBottomsheetCityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CityListDialogFragment : BottomSheetDialogFragment() {

    @Inject lateinit var sessionManager: SessionManager
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentBottomsheetCityBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottomsheet_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.cityRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CityListAdapter(::selectCity, sessionManager.fetchCurrentCityId())
        binding.cityRecyclerView.adapter = adapter

        viewModel.fetchCity()

        viewModel.cityResult.observe(this, Observer {
            val cityResult = it ?: return@Observer
            when (cityResult.status) {
                Result.Status.LOADING -> {
                    binding.cityLoadProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.cityLoadProgressbar.visibility = View.GONE
                    if (it.data?.size!! > 0) {
                        adapter.submitList(it.data)
                    } else {
                        binding.cityRecyclerView.visibility = View.GONE
                        binding.cityNotFoundTextView.visibility = View.VISIBLE
                    }
                }
                Result.Status.ERROR -> {
                    binding.cityLoadProgressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), cityResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun selectCity(position: Int) {
        val currentCity: City = viewModel.cityResult.value?.data!![position]
        viewModel.saveCurrentCity(currentCity)
        sessionManager.saveCurrentCityId(currentCity.id!!)
        sessionManager.saveCurrentCityName(currentCity.name!!)
    }

}