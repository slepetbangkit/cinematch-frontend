package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddReviewResponse(

	@field:SerializedName("data")
	val data: ReviewData,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: ReviewErrorMessage
)

data class ReviewData(

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

data class ReviewErrorMessage(

	@field:SerializedName("non_field_errors")
	val nonFieldErrors: List<String>
)

