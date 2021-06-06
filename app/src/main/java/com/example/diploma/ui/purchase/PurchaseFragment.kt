package com.example.diploma.ui.purchase

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.data.model.Result
import com.example.diploma.databinding.FragmentPurchaseBinding
import com.example.diploma.ui.history.HistoryViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PurchaseFragment : Fragment() {

    @Inject lateinit var sessionManager: SessionManager
    private val viewModel: HistoryViewModel by activityViewModels()
    private val navArgs: PurchaseFragmentArgs by navArgs()
    private lateinit var binding: FragmentPurchaseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_purchase,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPieChart()
        setupPieChartLegend()

        viewModel.fetchHistoryById(sessionManager.fetchAuthToken()!!, navArgs.id)

        viewModel.historyByIdResult.observe(viewLifecycleOwner, Observer {
            val historyByIdResult = it ?: return@Observer
            when (historyByIdResult.status) {
                Result.Status.LOADING -> {
                    binding.purchaseFragmentProgressbar.visibility = View.VISIBLE
                }
                Result.Status.SUCCESS -> {
                    binding.purchaseFragmentProgressbar.visibility = View.GONE
                    loadPieChartData(historyByIdResult.data!!)
                }
                Result.Status.ERROR -> {
                    binding.purchaseFragmentProgressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), historyByIdResult.error?.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupPieChart() {
        binding.purchaseFragmentPieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(12f)
            setEntryLabelColor(Color.BLACK)
            centerText = "Spending by Category"
            setCenterTextSize(24f)
            description.isEnabled = false
        }
    }

    private fun setupPieChartLegend() {
        binding.purchaseFragmentPieChart.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            orientation = Legend.LegendOrientation.VERTICAL
            setDrawInside(false)
            isEnabled = true
        }
    }

    private fun loadPieChartData(categoryExpenditures: List<CategoryExpenditure>) {
        val dataSet = PieDataSet(getPieChartEntries(categoryExpenditures), "Expense Category")
        dataSet.colors = getPieChartColors()
        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(binding.purchaseFragmentPieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)
        binding.purchaseFragmentPieChart.data = data
        binding.purchaseFragmentPieChart.invalidate()
        binding.purchaseFragmentPieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    private fun getPieChartEntries(categoryExpenditures: List<CategoryExpenditure>): ArrayList<PieEntry> {
        val totalAmount: Double = categoryExpenditures.sumOf { c -> c.amount ?: 0.0 }
        val entries: ArrayList<PieEntry> = ArrayList()
        categoryExpenditures.forEach {
            entries.add(PieEntry((it.amount?.div(totalAmount))!!.toFloat(), it.category))
        }
        return entries
    }

    private fun getPieChartColors(): ArrayList<Int> {
        val colors: ArrayList<Int> = ArrayList()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }
        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }
        return colors
    }

}
