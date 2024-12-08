package com.afrinacapstone.ecocraft.ui.detection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.ActivityDetectionResultBinding

class DetectionResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectionResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetectionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detectedImageResource = intent.getIntExtra("detectedImage", R.drawable.sampel_sampah)
        val wasteType = intent.getStringExtra("wasteType") ?: "Kardus"
        val craftIdea = intent.getStringExtra("craftIdea") ?: "Craft idea based on the waste type"

        binding.detectedImage.setImageResource(detectedImageResource)
        binding.wasteType.text = wasteType
        binding.sampleCraftIdea.text = craftIdea

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
