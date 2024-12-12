package com.afrinacapstone.ecocraft.model

import com.afrinacapstone.ecocraft.api.response.HistoryItem


data class PredictionHistory(
    val result: String,

    val createdAt: String,

    val id: String
)

fun HistoryItem.toPredictionHistory() = PredictionHistory(
    result = result,
    createdAt = createdAt,
    id = id
)