package com.afrinacapstone.ecocraft.api.response

import com.google.gson.annotations.SerializedName

data class LoginSuccessResponse(

	@field:SerializedName("data")
	val data: Token
)

data class Token(

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("refreshToken")
	val refreshToken: String
)

data class LoginErrorResponse(
	@SerializedName("error")
	val error: LoginErrorDetails
)

data class LoginErrorDetails(
	@SerializedName("message")
	val message: String,

	@SerializedName("details")
	val details: Map<String, Any>
)
