package com.example.diploma.ui.purchaseList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diploma.R

class PurchaseListFragment : Fragment() {

    companion object {
        fun newInstance() = PurchaseListFragment()
    }

    private lateinit var viewModel: PurchaseListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.purchase_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PurchaseListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}