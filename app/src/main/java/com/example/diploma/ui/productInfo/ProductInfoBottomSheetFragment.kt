package com.example.diploma.ui.productInfo

import android.content.DialogInterface
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
import com.example.diploma.databinding.FragmentBottomsheetProductInfoBinding
import com.example.diploma.ui.home.HomeViewModel
import com.example.diploma.ui.scan.CheaperProductListAdapter
import com.example.diploma.ui.scan.ProductInfoFragmentDismiss
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductInfoBottomSheetFragment(
    private val barcodeScanStateListener: ProductInfoFragmentDismiss) : BottomSheetDialogFragment() {

    @Inject lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentBottomsheetProductInfoBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottomsheet_product_info,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val barcode: String = arguments?.get("barcode") as String
        val supermarketId: Int = arguments?.get("supermarketId") as Int

        binding.productCheaperOptionsTextView.setOnClickListener {
            viewModel.fetchCheaperProduct(
                barcode,
                sessionManager.fetchCurrentCityId(),
                viewModel.product.value?.data?.price!!
            )
        }

        binding.productBuyButton.setOnClickListener {
            if (viewModel.product.value?.data != null) {
                viewModel.addProductToPurchaseList()
            } else {
                Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchProduct(barcode, supermarketId)

        viewModel.product.observe(this, Observer {
            val product = it ?: return@Observer
            when (product.status) {
                Result.Status.LOADING -> {
                    binding.productLoadProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.productLoadProgressbar.visibility = View.GONE
                    binding.productNameTextView.text = it.data?.product?.name
                    binding.productPriceTextView.text = it.data?.price.toString()
                    binding.productCategoryTextView.text = it.data?.product?.category?.name
                    binding.productBuyButton.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    binding.productLoadProgressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), product.message, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        })

        viewModel.cheaperProductResult.observe(this, Observer {
            val cheaperProduct = it ?: return@Observer
            when (cheaperProduct.status) {
                Result.Status.LOADING -> {
                    binding.productLoadProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.productLoadProgressbar.visibility = View.GONE
                    binding.productCheaperRecyclerView.visibility = View.VISIBLE
                    binding.productCheaperRecyclerView.layoutManager = LinearLayoutManager(context)
                    val cheapProductAdapter = CheaperProductListAdapter()
                    binding.productCheaperRecyclerView.adapter = cheapProductAdapter
                    cheapProductAdapter.submitList(it.data)
                }
                Result.Status.ERROR -> {
                    binding.productLoadProgressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), cheaperProduct.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.clearCheaperProductList()
        binding.productCheaperRecyclerView.visibility = View.GONE
        barcodeScanStateListener.toggleBarcodeScanningStateOnDismiss()
        super.onDismiss(dialog)
    }
}
