package com.afrinacapstone.ecocraft.ui.detection

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.afrinacapstone.ecocraft.MainActivity
import com.afrinacapstone.ecocraft.MainViewModel
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.FragmentDetectionBinding
import com.afrinacapstone.ecocraft.ui.detection.result.DetectionResultFragment
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.hasRequiredPermissions
import com.afrinacapstone.ecocraft.util.loadingDialog
import com.afrinacapstone.ecocraft.util.navigateById
import com.afrinacapstone.ecocraft.util.navigateWithBundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class DetectionFragment : Fragment() {

    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetectionViewModel by viewModels()
    private val mainViewModel by activityViewModels<MainViewModel>()


    companion object {
        fun newInstance() = DetectionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.afterGotImageGalery.collect { imageData ->
                    imageData?.path?.let { path ->
                        viewModel.setImagePath(path)
                        viewModel.postPrediction(File(path))
                        mainViewModel.clearImageGalleryData()
                    }
                }
            }
        }

        val loadingDialog = loadingDialog(requireContext())

        viewModel.resetPredictionUiState()

        viewModel.predictionUiState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> loadingDialog.show()

                is Resource.Success -> {
                    loadingDialog.dismiss()
                    navigateWithBundle(
                        R.id.action_nav_detection_to_detectionResultFragment,
                        bundle = Bundle().apply {
                            putParcelable(DetectionResultFragment.PREDICTION_DATA, it.data!!)
                            putString(DetectionResultFragment.IMAGE_PATH, viewModel.imagePath)
                        })
                }

                is Resource.Error -> {
                    loadingDialog.dismiss()
                    showErrorDialog(
                        title = getString(R.string.something_went_wrong),
                        message = it.message.toString()
                    )
                }

                else -> {}
            }
        }

        binding.setupViews()
    }

    private fun FragmentDetectionBinding.setupViews() {
        cameraButton.setOnClickListener {
            checkCameraPermission()
        }

        galleryButton.setOnClickListener {
            (requireActivity() as MainActivity).openGalleryPicker()
        }

        historyButton.setOnClickListener {
            navigateById(R.id.action_nav_detection_to_historyFragment)
        }
    }

    private val permissionCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                checkCameraPermission()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.give_permission_to_access_your_camera),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun checkCameraPermission() {
        if (hasRequiredPermissions(requireContext(), arrayOf(Manifest.permission.CAMERA))) {
            (activity as MainActivity).takePictureFromCamera()
        } else {
            permissionCamera.launch(Manifest.permission.CAMERA)
        }
    }

    private fun showErrorDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
