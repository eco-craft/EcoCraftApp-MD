package com.afrinacapstone.ecocraft.repository

import com.afrinacapstone.ecocraft.api.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject

class PredictionRepository @Inject constructor(private val api: ApiService) {

    suspend fun postPrediction(materialImage: MultipartBody.Part) =
        api.postPrediction(materialImage)

    suspend fun getPredictionHistory() =
        api.getPredictionHistory()

    suspend fun getPrediction(id: String) = api.getPrediction(id)
}