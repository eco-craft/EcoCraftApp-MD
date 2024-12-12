package com.afrinacapstone.ecocraft.ui.detection.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrinacapstone.ecocraft.api.response.PredictionHistoryResponse
import com.afrinacapstone.ecocraft.model.PredictionHistory
import com.afrinacapstone.ecocraft.model.toPredictionHistory
import com.afrinacapstone.ecocraft.repository.PredictionRepository
import com.afrinacapstone.ecocraft.util.DateUtils.parseDate
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.parseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val predictionRepository: PredictionRepository) :
    ViewModel() {

    private val _predictionHistoryUiState =
        MutableLiveData<Resource<List<PredictionHistory>>>(Resource.Init())
    val predictionHistoryUiState: LiveData<Resource<List<PredictionHistory>>> =
        _predictionHistoryUiState

    init {
        getPredictionHistory()
    }

    private fun getPredictionHistory() {
        viewModelScope.launch {
            _predictionHistoryUiState.value = Resource.Loading()
            try {
                val response = predictionRepository.getPredictionHistory()

                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val data = parseResponse<PredictionHistoryResponse>(responseBody)
                        val predictionHistoryList = data.data.map { it.toPredictionHistory() }
                            .sortedBy { it.createdAt.parseDate() }
                        _predictionHistoryUiState.value = Resource.Success(predictionHistoryList)
                    }
                } else {
                    _predictionHistoryUiState.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _predictionHistoryUiState.value = Resource.Error(e.message.toString())
            }
        }
    }
}