package com.example.diploma.ui.scan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.diploma.R
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.databinding.FragmentBarcodeScanBinding
import com.example.diploma.ui.home.HomeViewModel
import com.example.diploma.ui.productInfo.ProductInfoDialogFragment
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
typealias BarcodeListener = (barcode: String) -> Unit

@AndroidEntryPoint
class BarcodeScanFragment : Fragment() {

    @Inject lateinit var sessionManager: SessionManager
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var camera: Camera
    private lateinit var binding: FragmentBarcodeScanBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener( {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
        viewModel.setBarcodeProcessing(true)
        Log.d("MALBEK", "onCreate")
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

    override fun onDestroy() {
        cameraExecutor.shutdown()
        super.onDestroy()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        cameraProvider.unbindAll()
        val preview : Preview = Preview.Builder()
                .build()

        val cameraSelector : CameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcode ->
                        if (viewModel.barcodeProcessing.value!!) {
                            val bundle = Bundle()
                            bundle.putString("barcode", barcode)
                            bundle.putInt("supermarketId", sessionManager.fetchCurrentSupermarketId())
                            val productInfoBottomSheetDialogFragment = ProductInfoDialogFragment()
                            productInfoBottomSheetDialogFragment.arguments = bundle
                            productInfoBottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "")
                            viewModel.setBarcodeProcessing(false)
                        }
                    })
                }

        camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, imageAnalysis)
        if (!camera.cameraInfo.hasFlashUnit()) {
            binding.flashLightButton.isClickable = false
        }

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

}