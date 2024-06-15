package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: Any,

	@field:SerializedName("token")
	val token: Token
)

data class Token(

	@field:SerializedName("access")
	val access: String,

	@field:SerializedName("refresh")
	val refresh: String
)
