package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class RefreshResponse(

	@field:SerializedName("access")
	val access: String,

	@field:SerializedName("refresh")
	val refresh: String
)

