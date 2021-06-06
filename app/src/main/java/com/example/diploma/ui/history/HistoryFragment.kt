package com.example.diploma.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.data.model.Result
import com.example.diploma.databinding.FragmentHistoryBinding
import com.example.diploma.ui.home.HomeViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment(), PurchaseItemClickListener {

    @Inject lateinit var sessionManager: SessionManager
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_history,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HistoryListAdapter(requireContext(), this)

        binding.historyFragmentRecyclerView.adapter = adapter
        binding.historyFragmentRecyclerView.layoutManager = LinearLayoutManager(context)

        if (sessionManager.fetchAuthToken() != null) {
            viewModel.fetchHistory(sessionManager.fetchAuthToken()!!)
        }

        viewModel.historyResult.observe(viewLifecycleOwner, Observer {
            val historyResult = it ?: return@Observer
            when (historyResult.status) {
                Result.Status.LOADING -> {
                    binding.historyFragmentProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.historyFragmentProgressbar.visibility = View.GONE
                    adapter.submitList(historyResult.data)
                }
                Result.Status.ERROR -> {
                    binding.historyFragmentProgressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), historyResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

        binding.historyFragmentCalendarButton.setOnClickListener {
            dateRangePicker.show(requireActivity().supportFragmentManager, "")
        }

        dateRangePicker.addOnPositiveButtonClickListener {
            viewModel.fetchHistoryByDateRange(
                sessionManager.fetchAuthToken()!!,
                it.first!! - 21600000,
                it.second!! + 64799999)
        }
    }

    override fun onClickItem(id: Int) {
        findNavController().navigate(HistoryFragmentDirections.actionHistoryFragmentToPurchaseFragment(id))
    }

}
