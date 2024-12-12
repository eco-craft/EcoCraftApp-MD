package com.afrinacapstone.ecocraft.api.response

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

	@field:SerializedName("data")
	val data: UserData
)

data class UserData(

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
