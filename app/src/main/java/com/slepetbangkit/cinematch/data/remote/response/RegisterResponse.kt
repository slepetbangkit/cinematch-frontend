package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: Any? = null,

	@field:SerializedName("token")
	val token: Token? = null
)

data class Token(

	@field:SerializedName("access")
	val access: String? = null,

	@field:SerializedName("refresh")
	val refresh: String? = null
)

data class ErrorMessage(
	@field:SerializedName("username")
	val username: List<String>? = null,

	@field:SerializedName("email")
	val email: List<String>? = null,

	@field:SerializedName("password")
	val password: List<String>? = null
)
