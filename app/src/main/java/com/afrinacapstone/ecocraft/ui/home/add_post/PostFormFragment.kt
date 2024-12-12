package com.afrinacapstone.ecocraft.ui.home.add_post

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
import com.afrinacapstone.ecocraft.databinding.FragmentPostFormBinding
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.hasRequiredPermissions
import com.afrinacapstone.ecocraft.util.loadFromPath
import com.afrinacapstone.ecocraft.util.loadFromUrl
import com.afrinacapstone.ecocraft.util.loadingDialog
import com.afrinacapstone.ecocraft.util.navigateById
import com.afrinacapstone.ecocraft.util.navigateUp
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class PostFormFragment : Fragment() {

    private var _binding: FragmentPostFormBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<PostFormViewModel>()

    private val craft by lazy {
        arguments?.getParcelable(CRAFT_ITEM, com.afrinacapstone.ecocraft.model.Craft::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupViews()

        craft?.let {
            binding.apply {
                titleInput.setText(it.title)
                materialInput.setText(it.materials.joinToString())
                descriptionInput.setText(it.steps.joinToString())
                imageView.loadFromUrl(it.image)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.afterGotImageGalery.collect { imageData ->
                    imageData?.path?.let { path ->
                        viewModel.setImagePath(path)
                        binding.imageView.loadFromPath(path)
                        mainViewModel.clearImageGalleryData()
                    }
                }
            }
        }

        val loadingDialog = loadingDialog(requireContext())

        viewModel.postCraftUiState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }

                is Resource.Success -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                    navigateUp()
                }

                is Resource.Error -> {
                    loadingDialog.dismiss()
                    showErrorDialog(getString(R.string.something_went_wrong), it.message.toString())
                }

                else -> {}
            }
        }

        viewModel.editCraftUiState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }

                is Resource.Success -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                    navigateById(R.id.action_postFormFragment_to_nav_home)
                }

                is Resource.Error -> {
                    loadingDialog.dismiss()
                    showErrorDialog(getString(R.string.something_went_wrong), it.message.toString())
                }

                else -> {}
            }
        }
    }

    private fun FragmentPostFormBinding.setupViews() {
        backButton.setOnClickListener {
            navigateUp()
        }

        buttonCamera.setOnClickListener {
            checkCameraPermission()
        }

        buttonGallery.setOnClickListener {
            (requireActivity() as MainActivity).openGalleryPicker()
        }

        buttonSave.setOnClickListener {
            if (craft == null) {
                saveCraft()
            } else {
                updateCraft()
            }
        }

    }


    private fun FragmentPostFormBinding.updateCraft() {
        val title = titleInput.text.toString().takeIf { it != craft!!.title }
        val material = materialInput.text.toString()
        val steps = descriptionInput.text.toString()
        val image = viewModel.imagePath?.let { File(it) }

        viewModel.updateCraft(
            craft!!.id,
            title,
            material,
            steps,
            image
        )
    }

    private fun FragmentPostFormBinding.saveCraft() {
        var formValid = true

        if (titleInput.text.isNullOrEmpty()) {
            titleInput.error = getString(R.string.field_is_required, "Title")
            formValid = false
        }

        if (materialInput.text.isNullOrEmpty()) {
            materialInput.error = getString(R.string.field_is_required, "Materials")
            formValid = false
        }

        if (descriptionInput.text.isNullOrEmpty()) {
            descriptionInput.error = getString(R.string.field_is_required, "Description")
            formValid = false
        }

        if (viewModel.imagePath.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.image_is_required),
                Toast.LENGTH_SHORT
            ).show()
            formValid = false
        }


        if (formValid) {
            viewModel.postCraft(
                title = titleInput.text.toString(),
                materials = materialInput.text.toString(),
                description = descriptionInput.text.toString(),
                craftImage = File(viewModel.imagePath!!)
            )
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

    companion object {
        const val CRAFT_ITEM = "craft_item"
    }
}
