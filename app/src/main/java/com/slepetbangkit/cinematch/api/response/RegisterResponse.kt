package com.slepetbangkit.cinematch.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
