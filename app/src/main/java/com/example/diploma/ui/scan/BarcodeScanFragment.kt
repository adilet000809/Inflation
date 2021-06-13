package com.example.diploma.ui.scan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.databinding.FragmentBarcodeScanBinding
import com.example.diploma.ui.productInfo.ProductInfoBottomSheetFragment
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
typealias BarcodeListener = (barcode: String) -> Unit

@AndroidEntryPoint
class BarcodeScanFragment : Fragment(), ProductInfoFragmentDismiss {

    @Inject lateinit var sessionManager: SessionManager
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var camera: Camera
    private lateinit var binding: FragmentBarcodeScanBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener( {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_barcode_scan,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        var isFlashLightActive = false
        binding.flashLightButton.setOnClickListener {
            isFlashLightActive = if (isFlashLightActive) {
                camera.cameraControl.enableTorch(false)
                binding.flashLightButton.setImageResource(R.drawable.icon_flash_off)
                false
            } else {
                camera.cameraControl.enableTorch(true)
                binding.flashLightButton.setImageResource(R.drawable.icon_flash_on)
                true
            }
        }

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        cameraProvider.unbindAll()
        val preview : Preview = Preview.Builder()
                .build()
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, getCameraSelector(), preview, getImageAnalyzer())
        if (!camera.cameraInfo.hasFlashUnit()) {
            binding.flashLightButton.isClickable = false
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getCameraSelector(): CameraSelector {
        return CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }

    private fun getImageAnalyzer(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcode ->
                    if (BARCODE_SCANNING_STATE) {
                        showProductInfoFragment(barcode)
                    }
                })
            }
    }

    private fun showProductInfoFragment(barcode: String) {
        val bundle = Bundle()
        bundle.putString("barcode", barcode)
        bundle.putInt("supermarketId", sessionManager.fetchCurrentSupermarketId())
        val productInfoBottomSheetDialogFragment = ProductInfoBottomSheetFragment(this)
        productInfoBottomSheetDialogFragment.arguments = bundle
        productInfoBottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "")
        BARCODE_SCANNING_STATE = false
    }

    override fun onDestroy() {
        cameraExecutor.shutdown()
        super.onDestroy()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private var BARCODE_SCANNING_STATE = true
    }

    override fun toggleBarcodeScanningStateOnDismiss() {
        BARCODE_SCANNING_STATE = !BARCODE_SCANNING_STATE
    }

}

interface ProductInfoFragmentDismiss {
    fun toggleBarcodeScanningStateOnDismiss();
}