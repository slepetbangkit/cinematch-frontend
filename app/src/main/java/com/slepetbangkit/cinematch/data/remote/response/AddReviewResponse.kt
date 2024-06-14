package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddReviewResponse(

	@field:SerializedName("data")
	val data: ReviewData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: ReviewErrorMessage? = null
)

data class ReviewData(

	@field:SerializedName("movie")
	val movie: String? = null,

	@field:SerializedName("rating")
	val rating: Any? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("user")
	val user: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class ReviewErrorMessage(

	@field:SerializedName("non_field_errors")
	val nonFieldErrors: List<String>? = null
)

