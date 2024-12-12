package com.afrinacapstone.ecocraft

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrinacapstone.ecocraft.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _afterGotImageGalery = MutableStateFlow<ImageData?>(null)
    val afterGotImageGalery = _afterGotImageGalery.asStateFlow()

    val imagePathFromCamera = MutableLiveData<Event<String?>>()

    fun setImagePathFromCamera(path: String?) {
        imagePathFromCamera.value = Event(path)
    }

    fun setImageGalleryData(imageData: ImageData) {
        _afterGotImageGalery.value = imageData
    }

    fun clearImageGalleryData() {
        _afterGotImageGalery.value = null
    }
}

data class ImageData(val uri: Uri?, val isFromGalery: Boolean, val path: String?)
