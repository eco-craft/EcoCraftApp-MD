package com.afrinacapstone.ecocraft.api.response

import com.google.gson.annotations.SerializedName

data class GetHistoryPredictionItemResponse(
	@SerializedName("data")
	val data: DataHistoryPrediction
)

data class DataHistoryPrediction(
	@SerializedName("result")
	val result: String,
	@SerializedName("crafts")
	val crafts: List<CraftsItemHistoryPrediction>,
	@SerializedName("percentage")
	val percentage: String,
	@SerializedName("id")
	val id: String,
	@SerializedName("materialImage")
	val materialImage: String
)

data class CraftsItemHistoryPrediction(
	@SerializedName("image")
	val image: String,
	@SerializedName("createdAt")
	val createdAt: String,
	@SerializedName("materials")
	val materials: List<String>,
	@SerializedName("id")
	val id: String,
	@SerializedName("title")
	val title: String,
	@SerializedName("steps")
	val steps: List<String>,
)

