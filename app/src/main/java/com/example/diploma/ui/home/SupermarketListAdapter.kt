package com.example.diploma.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diploma.R

class SupermarketListAdapter(
    val supermarketClickListener: (position: Int) -> Unit,
    val currentSupermarketId: Int
): ListAdapter<Supermarket, SupermarketListAdapter.SupermarketViewHolder>(SUPERMARKET_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupermarketViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.supermarket_item_bottomsheet_dialog_fragment, parent, false)
        return SupermarketViewHolder(view)
    }

    override fun onBindViewHolder(holder: SupermarketViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name ?: "", position, current.id ?: -1)
    }

    inner class SupermarketViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val supermarketRadioButton: RadioButton = itemView.findViewById(R.id.supermarket_radio_button)
        private val supermarketTextView: TextView = itemView.findViewById(R.id.home_fragment_supermarket_text_view)

        fun bind(name: String, position: Int, supermarketId: Int) {
            supermarketTextView.text = name
            if (currentSupermarketId == supermarketId) {
                supermarketRadioButton.isChecked = true
            }
            if (LAST_SELECTED_POSITION == -1) {
                supermarketRadioButton.isChecked = false
            } else {
                supermarketRadioButton.isChecked = LAST_SELECTED_POSITION == position
            }
            supermarketTextView.setOnClickListener {
                supermarketClickListener(position)
                if (LAST_SELECTED_POSITION != position) {
                    notifyDataSetChanged()
                    LAST_SELECTED_POSITION = position
                }
            }
        }

    }

    companion object {
        private val SUPERMARKET_COMPARATOR = object : DiffUtil.ItemCallback<Supermarket>() {
            override fun areItemsTheSame(oldItem: Supermarket, newItem: Supermarket): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Supermarket, newItem: Supermarket): Boolean {
                return oldItem.id == newItem.id
            }
        }

        private var LAST_SELECTED_POSITION = -1
    }

}