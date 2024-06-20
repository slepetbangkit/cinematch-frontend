package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class ReviewDetailsResponse(

	@field:SerializedName("data")
	val data: ReviewDetailsData,

	@field:SerializedName("error")
	val error: Boolean
)

data class ReviewDetailsData(

	@field:SerializedName("sentiment")
	val sentiment: String,

	@field:SerializedName("movie")
	val movie: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("release_date")
	val releaseDate: String,

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
