package com.afrinacapstone.ecocraft.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.FragmentProfileBinding
import com.afrinacapstone.ecocraft.ui.auth.LoginActivity
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.loadFromUrl
import com.afrinacapstone.ecocraft.util.navigateById
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onStart() {
        super.onStart()
        viewModel.getUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel.userUiState.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        profileName.text = it.data?.data?.name
                        it.data?.data?.photo?.let { url -> profilePicture.loadFromUrl(url) }
                    }

                    is Resource.Error -> {
                        showToast(it.message.toString())
                    }

                    else -> {}
                }
            }

            editProfileLayout.setOnClickListener {
                navigateById(R.id.action_nav_profile_to_editProfileFragment)
            }

            editPasswordLayout.setOnClickListener {
                navigateById(R.id.action_nav_profile_to_editPasswordFragment)
            }

            logoutLayout.setOnClickListener {
                logout()
            }
        }
    }

    private fun logout() {
        viewModel.logout()
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
