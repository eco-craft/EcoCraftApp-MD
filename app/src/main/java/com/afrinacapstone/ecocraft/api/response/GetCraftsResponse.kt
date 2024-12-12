package com.afrinacapstone.ecocraft.api.response

import com.google.gson.annotations.SerializedName

data class GetCraftsResponse(

	@field:SerializedName("data")
	val data: List<GetCraftsItem>
)

data class GetCraftsItem(

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