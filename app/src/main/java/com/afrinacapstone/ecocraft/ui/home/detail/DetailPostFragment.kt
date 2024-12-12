package com.afrinacapstone.ecocraft.ui.home.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.FragmentDetailPostBinding
import com.afrinacapstone.ecocraft.model.Craft
import com.afrinacapstone.ecocraft.ui.home.add_post.PostFormFragment
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.loadFromUrl
import com.afrinacapstone.ecocraft.util.loadingDialog
import com.afrinacapstone.ecocraft.util.navigateUp
import com.afrinacapstone.ecocraft.util.navigateWithBundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class DetailPostFragment : Fragment() {

    private var _binding: FragmentDetailPostBinding? = null
    private val binding get() = _binding!!

    private val craft by lazy {
        arguments?.getParcelable(CRAFT_ITEM, Craft::class.java)
    }

    private val viewModel by viewModels<DetailPostViewModel>()

    private var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPostBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = runBlocking {
            viewModel.getUserId().firstOrNull().toString()
        }
        binding.setupViews()
    }

    private fun FragmentDetailPostBinding.setupViews() {
        val stepsBuilder = StringBuilder()

        craft?.let {
            postImage.loadFromUrl(it.image)
            it.steps.forEachIndexed { i, s ->
                stepsBuilder.append("${i + 1}. $s \n")
            }
            materialContent.text = it.materials.joinToString()
            descriptionContent.text = stepsBuilder
            title.text = it.title
        }

        val authorized = userId == craft?.user?.id

        ivDelete.isVisible = authorized
        fabEdit.isVisible = authorized

        backButton.setOnClickListener {
            navigateUp()
        }

        ivDelete.setOnClickListener {
            showConfirmationDialog {
                viewModel.deleteCraft(craft!!.id)
            }
        }

        val loadingDialog = loadingDialog(requireContext())

        viewModel.deleteCraftState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }

                is Resource.Success -> {
                    loadingDialog.dismiss()
                    navigateUp()
                    showToast(it.data.toString())
                }

                is Resource.Error -> {
                    loadingDialog.dismiss()
                    showToast(it.message.toString())
                }

                else -> {}
            }
        }

        fabEdit.setOnClickListener {
            navigateWithBundle(R.id.action_detailPostFragment_to_postFormFragment, Bundle().apply {
                putParcelable(PostFormFragment.CRAFT_ITEM, craft)
            })
        }
    }

    private fun showConfirmationDialog(onConfirm: () -> Unit = {}) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_post))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_post))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CRAFT_ITEM = "craft_item"
    }
}
