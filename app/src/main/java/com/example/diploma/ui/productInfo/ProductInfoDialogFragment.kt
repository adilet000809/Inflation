package com.example.diploma.ui.productInfo

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.data.model.Result
import com.example.diploma.ui.home.HomeViewModel
import com.example.diploma.ui.scan.CheaperProductListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductInfoDialogFragment : BottomSheetDialogFragment() {

    private lateinit var productCheaperRecyclerView: RecyclerView

    @Inject lateinit var sessionManager: SessionManager
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_bottomsheet_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val productNameTextView: TextView = view.findViewById(R.id.product_name_text_view)
        val productPriceTextView: TextView = view.findViewById(R.id.product_price_text_view)
        val productCategoryTextView: TextView = view.findViewById(R.id.product_category_text_view)
        val productCheaperOptionsTextView: TextView = view.findViewById(R.id.product_cheaper_options_text_view)
        productCheaperRecyclerView = view.findViewById(R.id.product_cheaper_recycler_view)
        val productBuyButton: Button = view.findViewById(R.id.product_buy_button)
        val productProgressBar: ProgressBar = view.findViewById(R.id.product_load_progressbar)
        val barcode: String = arguments?.get("barcode") as String
        val supermarketId: Int = arguments?.get("supermarketId") as Int

        productCheaperOptionsTextView.setOnClickListener {
            viewModel.fetchCheaperProduct(barcode, sessionManager.fetchCurrentCityId(),
                viewModel.product.value?.data?.price!!
            )
        }

        productBuyButton.setOnClickListener {
            if (viewModel.product.value?.data != null) {
                viewModel.addProductToPurchaseList(viewModel.product.value?.data!!)
            } else {
                Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchProduct(barcode, supermarketId)

        viewModel.product.observe(this, Observer {
            val product = it ?: return@Observer
            when (product.status) {
                Result.Status.LOADING -> {
                    productProgressBar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    productProgressBar.visibility = View.GONE
                    productNameTextView.text = it.data?.product?.name
                    productPriceTextView.text = it.data?.price.toString()
                    productCategoryTextView.text = it.data?.product?.category?.name
                    productBuyButton.visibility = View.VISIBLE
                }
                Result.Status.ERROR -> {
                    productProgressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), product.message, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        })

        viewModel.cheaperProductResult.observe(this, Observer {
            val cheaperProduct = it ?: return@Observer
            when (cheaperProduct.status) {
                Result.Status.LOADING -> {
                    productProgressBar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    productProgressBar.visibility = View.GONE
                    productCheaperRecyclerView.visibility = View.VISIBLE
                    productCheaperRecyclerView.layoutManager = LinearLayoutManager(context)
                    val cheapProductAdapter = CheaperProductListAdapter()
                    productCheaperRecyclerView.adapter = cheapProductAdapter
                    cheapProductAdapter.submitList(it.data)
                }
                Result.Status.ERROR -> {
                    productProgressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), cheaperProduct.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.setBarcodeProcessing(true)
        viewModel.clearCheaperProductList()
        productCheaperRecyclerView.visibility = View.GONE
        super.onDismiss(dialog)
    }

}
