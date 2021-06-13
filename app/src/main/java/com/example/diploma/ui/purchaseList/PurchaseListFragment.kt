package com.example.diploma.ui.purchaseList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.data.model.Result
import com.example.diploma.databinding.FragmentPurchaseListBinding
import com.example.diploma.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PurchaseListFragment : Fragment(), PurchaseProductItemClickListener {

    @Inject lateinit var sessionManager: SessionManager
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentPurchaseListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_purchase_list,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PurchaseListAdapter(this)

        binding.purchaseListRecyclerView.adapter = adapter
        binding.purchaseListRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter.submitList(viewModel.purchaseList.value)

        viewModel.purchaseList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            binding.purchaseConfirmButton.isEnabled = it.isNotEmpty()
            var total = 0.0
            it.forEach { product ->
                total += product.getTotal()
            }
            binding.purchaseTotalTextView.text = resources.getString(R.string.purchase_list_total, total.toString())
        })

        binding.purchaseConfirmButton.setOnClickListener {
            if (sessionManager.fetchAuthToken() != null) {
                viewModel.confirmPurchase(sessionManager.fetchAuthToken()!!)
            }
        }

        viewModel.purchaseResult.observe(viewLifecycleOwner, Observer {
            val purchaseResult = it ?: return@Observer
            when (purchaseResult.status) {
                Result.Status.LOADING -> {
                    binding.purchaseListProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.purchaseListProgressbar.visibility = View.GONE
                    viewModel.clearPurchaseList()
                }
                Result.Status.ERROR -> {
                    binding.purchaseListProgressbar.visibility = View.GONE
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

interface PurchaseProductItemClickListener {
    fun onDeleteItem(position: Int)
    fun onDecreaseQuantity(position: Int)
    fun onIncreaseQuantity(position: Int)
}
