package com.afrinacapstone.ecocraft.api.response

import com.google.gson.annotations.SerializedName

data class RegisterSuccessResponse(

	@field:SerializedName("data")
	val data: RegisterSuccessData,
)

data class RegisterSuccessData(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)

data class RegisterErrorResponse(
	@field:SerializedName("error")
	val error: RegisterErrorData
)

data class RegisterErrorData(
	@field:SerializedName("message")
	val message: String,
	@field:SerializedName("details")
	val details: RegisterErrorDetails
)

data class RegisterErrorDetails(
	@field:SerializedName("email")
	val email: List<String>,
	@field:SerializedName("password")
	val password: List<String>
)