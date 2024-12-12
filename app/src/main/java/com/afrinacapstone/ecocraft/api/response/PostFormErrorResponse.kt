package com.afrinacapstone.ecocraft.api.response

import com.google.gson.annotations.SerializedName

data class PostFormErrorResponse(

	@field:SerializedName("error")
	val error: Error
)

data class Details(

	@field:SerializedName("materials")
	val materials: List<String>,

	@field:SerializedName("title")
	val title: List<String>,

	@field:SerializedName("steps")
	val steps: List<String>
)

data class Error(

	@field:SerializedName("details")
	val details: Details,

	@field:SerializedName("message")
	val message: String
)
