package com.afrinacapstone.ecocraft.ui.detection.history

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.FragmentHistoryBinding
import com.afrinacapstone.ecocraft.ui.detection.result.DetectionResultFragment
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.loadingDialog
import com.afrinacapstone.ecocraft.util.navigateUp
import com.afrinacapstone.ecocraft.util.navigateWithBundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupViews()
    }

    private fun FragmentHistoryBinding.setupViews() {
        backButton.setOnClickListener {
            navigateUp()
        }

        emptyState.setEmptyState(getString(R.string.no_history_yet))

        val loadingDialog = loadingDialog(requireContext())

        setupRecyclerView()

        viewModel.predictionHistoryUiState.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Loading -> loadingDialog.show()
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    emptyState.isVisible = it.data.isNullOrEmpty()
                    historyAdapter.submitList(it.data)
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                }
                else -> {}
            }
        }
    }

    private fun FragmentHistoryBinding.setupRecyclerView() {
        historyAdapter = HistoryAdapter()
        recyclerViewHistory.adapter = historyAdapter
        recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())

        historyAdapter.onItemClicked = { history ->
            navigateWithBundle(R.id.action_historyFragment_to_detectionResultFragment, Bundle().apply {
                putString(DetectionResultFragment.PREDICTION_ID, history.id)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}