package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class ReviewDetailsResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("error")
	val error: Boolean
)

data class Data(

	@field:SerializedName("sentiment")
	val sentiment: String,

	@field:SerializedName("movie")
	val movie: String,

	@field:SerializedName("rating")
	val rating: Any,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("user")
	val user: String,

	@field:SerializedName("username")
	val username: String
)
