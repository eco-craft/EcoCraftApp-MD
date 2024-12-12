package com.afrinacapstone.ecocraft.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.FragmentHomeBinding
import com.afrinacapstone.ecocraft.ui.home.detail.DetailPostFragment
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.gone
import com.afrinacapstone.ecocraft.util.navigateById
import com.afrinacapstone.ecocraft.util.navigateWithBundle
import com.afrinacapstone.ecocraft.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var craftAdapter: CraftAdapter

    override fun onStart() {
        super.onStart()
        viewModel.getCrafts("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupViews()
    }

    private fun FragmentHomeBinding.setupViews() {
        searchView.setupWithSearchBar(searchBar)

        searchView
            .editText
            .setOnEditorActionListener { v, actionId, event ->
                searchBar.setText(searchView.text)
                searchView.hide()
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                    viewModel.getCrafts(searchBar.text.toString())
                false
            }

        setupRecyclerView()

        emptyState.setEmptyState(getString(R.string.crafts_empty))

        viewModel.craftsUiState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    progressBar.visible()
                    emptyState.gone()
                }

                is Resource.Success -> {
                    progressBar.gone()
                    emptyState.gone()
                    craftAdapter.submitList(it.data)
                }

                is Resource.Error -> {
                    progressBar.gone()
                    emptyState.visible()
                    if (it.message.toString()
                            .contains("not found")
                    ) emptyState.setEmptyState(it.message.toString())
                }

                else -> {}
            }
        }

        btnAddPost.setOnClickListener {
            navigateById(R.id.action_nav_home_to_postFormFragment)
        }
    }

    private fun FragmentHomeBinding.setupRecyclerView() {
        craftAdapter = CraftAdapter()
        rvCrafts.adapter = craftAdapter
        rvCrafts.layoutManager = LinearLayoutManager(requireContext())

        craftAdapter.onClickListener = { craft ->
            navigateWithBundle(R.id.action_nav_home_to_detailPostFragment, bundle = Bundle().apply {
                putParcelable(DetailPostFragment.CRAFT_ITEM, craft)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
