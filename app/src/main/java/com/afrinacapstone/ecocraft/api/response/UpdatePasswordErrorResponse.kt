package com.afrinacapstone.ecocraft.api.response

import com.google.gson.annotations.SerializedName

data class UpdatePasswordErrorResponse(

	@field:SerializedName("error")
	val error: UpdatePasswordError
)

data class UpdatePasswordError(
	@field:SerializedName("message")
	val message: String
)
