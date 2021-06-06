package com.example.diploma.ui.scan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diploma.R
import com.example.diploma.ui.productInfo.SupermarketProduct

class CheaperProductListAdapter: ListAdapter<SupermarketProduct, CheaperProductListAdapter.CheaperProductViewHolder>(CHEAPER_PRODUCT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheaperProductViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cheaper_product_item, parent, false)
        return CheaperProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheaperProductViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.supermarket.name ?: "", current.price ?: 0.0)
    }

    inner class CheaperProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val cheaperProductSupermarketTextView: TextView = itemView.findViewById(R.id.cheaper_product_supermarket_text_view)
        private val cheaperProductPriceTextView: TextView = itemView.findViewById(R.id.cheaper_product_price_text_view)

        fun bind(supermarketName: String, price: Double) {
            cheaperProductSupermarketTextView.text = supermarketName
            cheaperProductPriceTextView.text = price.toString()
        }
    }

    companion object {
        private val CHEAPER_PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<SupermarketProduct>() {
            override fun areItemsTheSame(oldItem: SupermarketProduct, newItem: SupermarketProduct): Boolean {
                return oldItem === newItem
            }
            override fun areContentsTheSame(oldItem: SupermarketProduct, newItem: SupermarketProduct): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}