package com.afrinacapstone.ecocraft.ui.detection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.FragmentDetectionBinding

class DetectionFragment : Fragment() {

    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetectionViewModel by viewModels()

    companion object {
        fun newInstance() = DetectionFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.instructionText1.observe(this) { text ->
            binding.txt1.text = text
        }
        viewModel.instructionText2.observe(this) { text ->
            binding.txt2.text = text
        }
        viewModel.detectionWord.observe(this) { text ->
            binding.selectImageText.text = text
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cameraButton.setOnClickListener {
        }

        binding.galleryButton.setOnClickListener {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
