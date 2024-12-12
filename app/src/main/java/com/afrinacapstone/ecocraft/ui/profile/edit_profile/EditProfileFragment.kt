package com.afrinacapstone.ecocraft.ui.profile.edit_profile

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.afrinacapstone.ecocraft.MainActivity
import com.afrinacapstone.ecocraft.MainViewModel
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.FragmentEditProfileBinding
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.hasRequiredPermissions
import com.afrinacapstone.ecocraft.util.loadFromPath
import com.afrinacapstone.ecocraft.util.loadFromUrl
import com.afrinacapstone.ecocraft.util.loadingDialog
import com.afrinacapstone.ecocraft.util.navigateUp
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EditProfileViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingDialog = loadingDialog(requireContext())

        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainViewModel.afterGotImageGalery.collect { imageData ->
                        imageData?.path?.let { path ->
                            viewModel.setImagePath(path)
                            profileImage.loadFromPath(path)
                            mainViewModel.clearImageGalleryData()
                        }
                    }
                }
            }

            editProfilePicture.setOnClickListener {
                showImageSourceDialog()
            }

            backButton.setOnClickListener {
                navigateUp()
            }

            viewModel.userUiState.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> {
                        loadingDialog.show()
                    }

                    is Resource.Success -> {
                        loadingDialog.dismiss()
                        val user = it.data?.data!!
                        nameInput.setText(user.name)
                        phoneInput.setText(user.phone)
                        emailInput.setText(user.email)
                        addressInput.setText(user.address)
                        profileImage.loadFromUrl(user.photo)
                    }

                    is Resource.Error -> {
                        loadingDialog.dismiss()
                        showToast(it.message.toString())
                    }

                    else -> {}
                }
            }

            viewModel.updateProfileUiState.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> {
                        loadingDialog.show()
                    }

                    is Resource.Success -> {
                        loadingDialog.dismiss()
                        showToast(it.data.toString())
                        navigateUp()
                    }

                    is Resource.Error -> {
                        loadingDialog.dismiss()
                        showToast(it.message.toString())
                    }

                    else -> {}
                }
            }

            buttonSaveProfile.setOnClickListener {
                val user = viewModel.user.data

                val name = nameInput.text.toString().takeIf { it != user.name }
                val phone = phoneInput.text.toString().takeIf { it != user.phone }
                val email = emailInput.text.toString().takeIf { it != user.email }
                val address = addressInput.text.toString().takeIf { it != user.address }
                val image = viewModel.imagePath?.let { File(it) }

                viewModel.updateUserProfile(
                    name,
                    phone,
                    email,
                    address,
                    image
                )
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showImageSourceDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setSingleChoiceItems(arrayOf("Camera", "Gallery"), 0) { dialog, which ->
                when (which) {
                    0 -> {
                        checkCameraPermission()
                    }

                    1 -> {
                        (activity as MainActivity).openGalleryPicker()
                    }
                }
                dialog.dismiss()
            }
            .show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
