package com.afrinacapstone.ecocraft.ui.detection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.api.response.PostPredictionsResponse
import com.afrinacapstone.ecocraft.model.PredictionResult
import com.afrinacapstone.ecocraft.model.toPredictionResult
import com.afrinacapstone.ecocraft.repository.PredictionRepository
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.parseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetectionViewModel @Inject constructor(private val predictionRepository: PredictionRepository) :
    ViewModel() {

    private val _predictionUiState = MutableLiveData<Resource<PredictionResult>>()
    val predictionUiState: LiveData<Resource<PredictionResult>> = _predictionUiState

    private var _imagePath: String? = null
    val imagePath: String? get() = _imagePath

    fun setImagePath(path: String) {
        _imagePath = path
    }

    fun postPrediction(imageFile: File) {
        viewModelScope.launch {
            _predictionUiState.value = Resource.Loading()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val materialImage = MultipartBody.Part.createFormData(
                "materialImage",
                imageFile.name,
                requestImageFile
            )
            try {
                val response = predictionRepository.postPrediction(materialImage)
                when {
                    response.code() == 401 -> {
                        _predictionUiState.value = Resource.Error("User is not authorized")
                    }

                    response.isSuccessful -> {
                        response.body()?.let { responseBody ->
                            val data = parseResponse<PostPredictionsResponse>(responseBody)
                            val percentageValue = data.data.percentage.removeSuffix("%").toDouble()
                            val percentageLessThan50 = percentageValue < 50
                            when {
                                data.data.result.contains("No material found") -> {
                                    _predictionUiState.value = Resource.Error("No material found")
                                }

                                percentageLessThan50 -> {
                                    _predictionUiState.value = Resource.Error("No material found")
                                }

                                else -> {
                                    val predictionResult = data.toPredictionResult()
                                    _predictionUiState.value = Resource.Success(predictionResult)
                                }
                            }
                        }
                    }

                    else ->
                        _predictionUiState.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _predictionUiState.value = Resource.Error(e.toString())
            }
        }
    }

    fun resetPredictionUiState() {
        _predictionUiState.value = Resource.Init()
    }
}
