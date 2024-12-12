package com.afrinacapstone.ecocraft.ui.profile.edit_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.FragmentEditPasswordBinding
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.loadingDialog
import com.afrinacapstone.ecocraft.util.navigateUp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPasswordFragment : Fragment() {

    private var _binding: FragmentEditPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EditPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backButton.setOnClickListener {
                navigateUp()
            }

            buttonSavePassword.setOnClickListener {
                var formValid = true

                if (oldPasswordInput.text.toString().isEmpty()) {
                    oldPasswordInput.error = getString(R.string.field_is_required, "Old Password")
                    formValid = false
                }

                if (newPasswordInput.text.toString().isEmpty()) {
                    newPasswordInput.error = getString(R.string.field_is_required, "New Password")
                    formValid = false
                }

                if (confirmPasswordInput.text.toString().isEmpty()) {
                    confirmPasswordInput.error =
                        getString(R.string.field_is_required, "Confirm Password")
                    formValid = false
                }

                if (formValid) {
                    viewModel.updatePassword(
                        oldPasswordInput.text.toString(),
                        newPasswordInput.text.toString(),
                        confirmPasswordInput.text.toString()
                    )
                }
            }

            val loadingDialog = loadingDialog(requireContext())

            viewModel.updatePasswordState.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> loadingDialog.show()

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
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
