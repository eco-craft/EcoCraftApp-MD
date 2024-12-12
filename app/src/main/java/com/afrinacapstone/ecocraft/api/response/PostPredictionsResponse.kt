package com.afrinacapstone.ecocraft.api.response

import com.google.gson.annotations.SerializedName

data class PostPredictionsResponse(

	@field:SerializedName("data")
	val data: PredictionData
)

data class CraftsItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("materials")
	val materials: List<String>,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("steps")
	val steps: List<String>,

	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("id")
	val id: String
)

data class PredictionData(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("crafts")
	val crafts: List<CraftsItem>,

	@field:SerializedName("percentage")
	val percentage: String
)
