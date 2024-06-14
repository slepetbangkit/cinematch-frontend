package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieReviewsResponse(

	@field:SerializedName("movie")
	val movie: Movie? = null,

	@field:SerializedName("reviews")
	val reviews: List<ReviewsItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class Movie(

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class ReviewsItem(

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
