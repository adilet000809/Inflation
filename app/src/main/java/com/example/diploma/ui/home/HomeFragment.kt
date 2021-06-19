package com.example.diploma.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject lateinit var sessionManager: SessionManager
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private val sliderHandler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container, false
        )
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImageSlider()

        binding.homeFragmentCurrentCityTextView.text = sessionManager.fetchCurrentCityName() ?: ""
        binding.homeFragmentSupermarketTextView.text = sessionManager.fetchCurrentSupermarketName() ?: ""

        viewModel.currentCity.observe(viewLifecycleOwner, {
            binding.homeFragmentCurrentCityTextView.text = it.name ?: ""
        })

        viewModel.currentSupermarket.observe(viewLifecycleOwner, {
            binding.homeFragmentSupermarketTextView.text = it?.name ?: ""
        })

        binding.homeFragmentCityLocationImageView.setOnClickListener {
            val cityBottomSheetDialogFragment = CityBottomSheetFragment()
            cityBottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "")
        }

        binding.homeFragmentSupermarketImageView.setOnClickListener {
            val supermarketBottomSheetDialogFragment = SupermarketBottomSheetFragment()
            supermarketBottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "")
        }

    }

    private fun getImageSliderItems(): MutableList<ImageSlide> {
        return mutableListOf(
            ImageSlide(R.drawable.barcode_scan),
            ImageSlide(R.drawable.prices_compare),
            ImageSlide(R.drawable.purchase_audits)
        )
    }

    private fun setImageSliderAdapter() {
        val sliderAdapter = SliderAdapter(getImageSliderItems(), binding.homeFragmentImageSliderViewPager)
        binding.homeFragmentImageSliderViewPager.apply {
            adapter = sliderAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

    }

    private fun getCompositePageTransformer(): CompositePageTransformer {
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            run {
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }
        }
        return compositePageTransformer
    }

    private fun initImageSlider() {
        setImageSliderAdapter()
        binding.homeFragmentImageSliderViewPager.setPageTransformer(getCompositePageTransformer())
        val sliderRunnable = Runnable {
            binding.homeFragmentImageSliderViewPager.currentItem = binding.homeFragmentImageSliderViewPager.currentItem + 1 }
        binding.homeFragmentImageSliderViewPager.registerOnPageChangeCallback (object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 10000)
            }
        })
    }

}