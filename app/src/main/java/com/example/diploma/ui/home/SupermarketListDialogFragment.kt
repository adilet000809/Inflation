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
import com.example.diploma.databinding.FragmentBottomsheetSupermarketBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SupermarketListDialogFragment : BottomSheetDialogFragment() {

    @Inject lateinit var sessionManager: SessionManager
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentBottomsheetSupermarketBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottomsheet_supermarket,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.supermarketRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SupermarketListAdapter(::selectSupermarket, sessionManager.fetchCurrentSupermarketId())
        binding.supermarketRecyclerView.adapter = adapter

        if (sessionManager.fetchCurrentCityId() != -1) {
            viewModel.fetchSupermarket(sessionManager.fetchCurrentCityId())
        }

        viewModel.supermarketResult.observe(this, Observer {
            val supermarketResult = it ?: return@Observer
            when (supermarketResult.status) {
                Result.Status.LOADING -> {
                    binding.supermarketLoadProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.supermarketLoadProgressbar.visibility = View.GONE
                    if (it.data?.size!! > 0) {
                        adapter.submitList(it.data)
                    } else {
                        binding.supermarketNotFoundTextView.visibility = View.VISIBLE
                        binding.supermarketRecyclerView.visibility = View.GONE
                    }
                }
                Result.Status.ERROR -> {
                    binding.supermarketLoadProgressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), supermarketResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun selectSupermarket(position: Int) {
        val currentSupermarket: Supermarket = viewModel.supermarketResult.value?.data!![position]
        viewModel.saveCurrentSupermarket(currentSupermarket)
        sessionManager.saveCurrentSupermarketId(currentSupermarket.id!!)
        sessionManager.saveCurrentSupermarketName(currentSupermarket.name!!)
    }

}