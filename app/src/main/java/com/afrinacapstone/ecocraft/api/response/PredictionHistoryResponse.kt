package com.afrinacapstone.ecocraft.api.response

import com.google.gson.annotations.SerializedName

data class PredictionHistoryResponse(

    @field:SerializedName("data")
    val data: List<HistoryItem>
)

data class HistoryItem(

    @field:SerializedName("result")
    val result: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("id")
    val id: String
)
