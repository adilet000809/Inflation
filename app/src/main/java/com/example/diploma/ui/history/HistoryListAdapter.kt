package com.example.diploma.ui.history

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diploma.R
import java.text.SimpleDateFormat
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HistoryListAdapter(
    private val context: Context,
    val clickListener: PurchaseItemClickListener
): ListAdapter<PurchaseHistory, HistoryListAdapter.HistoryViewHolder>(HISTORY_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_list_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.date!!, current.total.toString(), current.id ?: -1)
    }

    inner class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val historyItemContainer: ConstraintLayout = itemView.findViewById(R.id.history_item_container)
        private val historyItemDateTextView: TextView = itemView.findViewById(R.id.history_item_date_text_view)
        private val historyItemTotalTextView: TextView = itemView.findViewById(R.id.history_item_total_text_view)

        fun bind(date: Date, total: String, id: Int) {
            historyItemDateTextView.text = DATE_FORMAT.format(date)
            historyItemTotalTextView.text = context.resources.getString(R.string.history_list_item_price, total)
            historyItemContainer.setOnClickListener {
                clickListener.onClickItem(id)
            }
        }
    }

    companion object {
        private val HISTORY_COMPARATOR = object : DiffUtil.ItemCallback<PurchaseHistory>() {
            override fun areItemsTheSame(oldItem: PurchaseHistory, newItem: PurchaseHistory): Boolean {
                return oldItem === newItem
            }
            override fun areContentsTheSame(oldItem: PurchaseHistory, newItem: PurchaseHistory): Boolean {
                return oldItem.id == newItem.id
            }
        }
        @SuppressLint("SimpleDateFormat")
        private val DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy")
    }

}

interface PurchaseItemClickListener {
    fun onClickItem(id: Int)
}
