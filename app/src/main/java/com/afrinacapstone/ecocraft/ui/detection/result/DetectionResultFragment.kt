package com.afrinacapstone.ecocraft.ui.detection.result

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.FragmentDetectionResultBinding
import com.afrinacapstone.ecocraft.model.PredictionResult
import com.afrinacapstone.ecocraft.ui.detection.CraftRecommendationAdapter
import com.afrinacapstone.ecocraft.ui.home.detail.DetailPostFragment
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.gone
import com.afrinacapstone.ecocraft.util.loadFromPath
import com.afrinacapstone.ecocraft.util.loadFromUrl
import com.afrinacapstone.ecocraft.util.loadingDialog
import com.afrinacapstone.ecocraft.util.navigateUp
import com.afrinacapstone.ecocraft.util.navigateWithBundle
import com.afrinacapstone.ecocraft.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class DetectionResultFragment : Fragment() {

    private var _binding: FragmentDetectionResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetectionResultViewModel>()

    private val predictionResult by lazy {
        arguments?.getParcelable<PredictionResult>(PREDICTION_DATA)
    }

    private val imagePath by lazy {
        arguments?.getString(IMAGE_PATH)
    }

    private val predictionId by lazy {
        arguments?.getString(PREDICTION_ID)
    }

    private lateinit var craftAdapter: CraftRecommendationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionResultBinding.inflate(inflater, container, false)
        predictionId?.let { viewModel.getPrediction(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DetectionResultFragment", "predictionResult $predictionResult")
        binding.setupViews()
    }

    private fun FragmentDetectionResultBinding.setupViews() {
        setupRecyclerView()

        wasteType.text = predictionResult?.result
        imagePath?.let { detectedImage.loadFromPath(it) }
        predictionResult?.let {
            craftAdapter.submitList(it.crafts)
        }

        backButton.setOnClickListener {
            navigateUp()
        }

        if (predictionResult?.crafts.isNullOrEmpty()) {
            emptyState.setEmptyState(getString(R.string.no_recommendation_found))
            emptyState.visible()
        } else {
            emptyState.gone()
        }

        val loadingDialog = loadingDialog(requireContext())

        viewModel.predictionResult.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> loadingDialog.show()
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    craftAdapter.submitList(it.data?.crafts)
                    it.data?.materialImage?.let { it1 -> detectedImage.loadFromUrl(it1) }
                    wasteType.text = it.data?.result
                    if (it.data?.crafts.isNullOrEmpty()) {
                        emptyState.setEmptyState(getString(R.string.no_recommendation_found))
                        emptyState.visible()
                    } else {
                        emptyState.gone()
                    }
                }

                is Resource.Error -> {
                    loadingDialog.dismiss()
                    emptyState.setEmptyState(it.message.toString())
                    emptyState.visible()
                }

                else -> {}
            }
        }

    }

    private fun FragmentDetectionResultBinding.setupRecyclerView() {
        craftAdapter = CraftRecommendationAdapter()
        rvRecommendation.adapter = craftAdapter
        rvRecommendation.layoutManager = LinearLayoutManager(requireContext())

        craftAdapter.onClickListener = { craft ->
            navigateWithBundle(
                R.id.action_detectionResultFragment_to_detailPostFragment,
                Bundle().apply {
                    putParcelable(DetailPostFragment.CRAFT_ITEM, craft)
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val PREDICTION_DATA = "prediction_data"
        const val IMAGE_PATH = "image_path"

        const val PREDICTION_ID = "prediction_id"
    }
}
