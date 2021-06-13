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

class CityListAdapter(
    val cityClickListener: (position: Int) -> Unit,
    val currentCityId: Int
): ListAdapter<City, CityListAdapter.CityViewHolder>(CITY_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item_bottomsheet_dialog_fragment, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name ?: "", position, current.id ?: -1)
    }

    inner class CityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val cityRadioButton: RadioButton = itemView.findViewById(R.id.city_radio_button)
        private val cityTextView: TextView = itemView.findViewById(R.id.city_text_view)

        fun bind(name: String, position: Int, cityId: Int) {
            cityTextView.text = name
            if (currentCityId == cityId) {
                cityRadioButton.isChecked = true
            }
            if (LAST_SELECTED_POSITION == -1) {
                cityRadioButton.isChecked = false
            } else {
                cityRadioButton.isChecked = LAST_SELECTED_POSITION == position
            }
            cityTextView.setOnClickListener {
                cityClickListener(position)
                if (LAST_SELECTED_POSITION != position) {
                    notifyDataSetChanged()
                    LAST_SELECTED_POSITION = position
                }
            }
        }

    }

    companion object {
        private val CITY_COMPARATOR = object : DiffUtil.ItemCallback<City>() {
            override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
                return oldItem === newItem
            }
            override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
                return oldItem.id == newItem.id
            }
        }
        private var LAST_SELECTED_POSITION = -1
    }
}
