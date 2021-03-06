package com.example.diploma.ui.home
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.data.model.Result
import com.example.diploma.databinding.FragmentBottomsheetCityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CityBottomSheetFragment : BottomSheetDialogFragment() {

    @Inject lateinit var sessionManager: SessionManager
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentBottomsheetCityBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottomsheet_city,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
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
        viewModel.clearSupermarket()
        sessionManager.saveCurrentCityId(currentCity.id!!)
        sessionManager.saveCurrentCityName(currentCity.name!!)
        sessionManager.clearSupermarket()
    }

}