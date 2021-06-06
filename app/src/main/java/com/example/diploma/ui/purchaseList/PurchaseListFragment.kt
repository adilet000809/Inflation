package com.example.diploma.ui.purchaseList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.data.model.Result
import com.example.diploma.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PurchaseListFragment : Fragment(), PurchaseProductItemClickListener {

    @Inject lateinit var sessionManager: SessionManager
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.purchase_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val purchaseTotalTextView: TextView = view.findViewById(R.id.purchase_total_text_view)
        val purchaseConfirmButton: Button = view.findViewById(R.id.purchase_confirm_button)
        val purchaseListProgressBar: ProgressBar = view.findViewById(R.id.purchase_list_progressbar)
        val purchaseListRecyclerView: RecyclerView = view.findViewById(R.id.purchase_list_recycler_view)

        val adapter = PurchaseListAdapter(this)

        purchaseListRecyclerView.adapter = adapter
        purchaseListRecyclerView.layoutManager = LinearLayoutManager(context)

        val a = viewModel.purchaseList.value

        adapter.submitList(a)

        viewModel.purchaseList.observe(viewLifecycleOwner, {
            purchaseConfirmButton.isEnabled = it.size > 0
            var total = 0.0
            it.forEach { product ->
                total += product.getTotal()
            }
            purchaseTotalTextView.text = resources.getString(R.string.purchase_list_total, total.toString())
        })

        purchaseConfirmButton.setOnClickListener {
            if (sessionManager.fetchAuthToken() != null) {
                viewModel.confirmPurchase(sessionManager.fetchAuthToken()!!)
            }
        }

        viewModel.purchaseResult.observe(viewLifecycleOwner, Observer {
            val purchaseResult = it ?: return@Observer
            when (purchaseResult.status) {
                Result.Status.LOADING -> {
                    purchaseListProgressBar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    purchaseListProgressBar.visibility = View.GONE
                    viewModel.clearPurchaseList()
                }
                Result.Status.ERROR -> {
                    purchaseListProgressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), purchaseResult.message, Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

    override fun onDeleteItem(position: Int) {
        viewModel.deletePurchaseListItem(position)
    }

    override fun onDecreaseQuantity(position: Int) {
        viewModel.decreaseQuantityPurchaseList(position)
    }

    override fun onIncreaseQuantity(position: Int) {
        viewModel.increaseQuantityPurchaseList(position)
    }

}
