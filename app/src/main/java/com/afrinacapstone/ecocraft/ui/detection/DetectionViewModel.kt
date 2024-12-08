package com.afrinacapstone.ecocraft.ui.detection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetectionViewModel : ViewModel() {
    private val _instructionText1 = MutableLiveData<String>()
    val instructionText1: LiveData<String> get() = _instructionText1

    private val _instructionText2 = MutableLiveData<String>()
    val instructionText2: LiveData<String> get() = _instructionText2

    private val _detectionWord = MutableLiveData<String>()
    val detectionWord: LiveData<String> get() = _detectionWord

    init {
        _instructionText1.value = "Take a photo to detect waste."
        _instructionText2.value = "Or choose an image from the gallery."
        _detectionWord.value = "Select Image"
    }

}
