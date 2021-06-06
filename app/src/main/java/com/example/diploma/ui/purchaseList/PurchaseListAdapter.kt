package com.example.diploma.ui.purchaseList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diploma.R

class PurchaseListAdapter(
    val clickListener: PurchaseProductItemClickListener
): ListAdapter<PurchaseListProduct, PurchaseListAdapter.PurchaseListViewHolder>(PURCHASE_ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.purchase_list_item, parent, false)
        return PurchaseListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchaseListViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.supermarketProduct.product.name ?: "", current.quantity, position)
    }

    inner class PurchaseListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val purchaseListProductNameTextView: TextView = itemView.findViewById(R.id.purchase_list_product_name_text_view)
        private val purchaseListProductQuantityTextView: TextView = itemView.findViewById(R.id.purchase_list_product_quantity_text_view)
        private val purchaseListQuantityDecreaseButton: AppCompatButton = itemView.findViewById(R.id.purchase_list_quantity_decrease_button)
        private val purchaseListQuantityIncreaseButton: AppCompatButton = itemView.findViewById(R.id.purchase_list_quantity_increase_button)
        private val purchaseListProductDeleteButton: ImageButton = itemView.findViewById(R.id.purchase_list_product_delete_button)

        fun bind(name: String, quantity: Int, position: Int) {
            purchaseListProductNameTextView.text = name
            purchaseListProductQuantityTextView.text = quantity.toString()
            purchaseListProductDeleteButton.setOnClickListener {
                clickListener.onDeleteItem(position)
                notifyItemRemoved(position)
            }
            purchaseListQuantityDecreaseButton.setOnClickListener {
                if (quantity > 1) {
                    purchaseListProductQuantityTextView.text = (quantity - 1).toString()
                    clickListener.onDecreaseQuantity(position)
                    notifyItemChanged(position)
                }
            }
            purchaseListQuantityIncreaseButton.setOnClickListener {
                purchaseListProductQuantityTextView.text = (quantity + 1).toString()
                clickListener.onIncreaseQuantity(position)
                notifyItemChanged(position)
            }
        }

    }

    companion object {
        private val PURCHASE_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<PurchaseListProduct>() {
            override fun areItemsTheSame(oldItem: PurchaseListProduct, newItem: PurchaseListProduct): Boolean {
                return oldItem === newItem
            }
            override fun areContentsTheSame(oldItem: PurchaseListProduct, newItem: PurchaseListProduct): Boolean {
                return oldItem.supermarketProduct.id == newItem.supermarketProduct.id
            }
        }
    }

}

interface PurchaseProductItemClickListener {
    fun onDeleteItem(position: Int)
    fun onDecreaseQuantity(position: Int)
    fun onIncreaseQuantity(position: Int)
}