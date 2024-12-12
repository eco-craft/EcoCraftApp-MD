package com.afrinacapstone.ecocraft.ui.detection.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.api.response.GetHistoryPredictionItemResponse
import com.afrinacapstone.ecocraft.model.PredictionResult
import com.afrinacapstone.ecocraft.model.toPredictionResult
import com.afrinacapstone.ecocraft.repository.PredictionRepository
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.parseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetectionResultViewModel @Inject constructor(private val predictionRepository: PredictionRepository) :
    ViewModel() {

    private var _predictionResult = MutableLiveData<Resource<PredictionResult>>()
    val predictionResult get() : LiveData<Resource<PredictionResult>> = _predictionResult

    fun getPrediction(id: String) {
        viewModelScope.launch {
            _predictionResult.value = Resource.Loading()
            try {
                val response = predictionRepository.getPrediction(id)

                if (response.isSuccessful) {
                    response.body()?.let {
                        val historyPredictionItem =
                            parseResponse<GetHistoryPredictionItemResponse>(it)
                        _predictionResult.value =
                            Resource.Success(historyPredictionItem.toPredictionResult())
                    }
                } else {
                    _predictionResult.value = Resource.Error("Fetch prediction failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _predictionResult.value = Resource.Error(e.message.toString())

            }
        }
    }
}