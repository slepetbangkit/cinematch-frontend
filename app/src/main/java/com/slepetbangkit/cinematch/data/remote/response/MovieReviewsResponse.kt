package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieReviewsResponse(

	@field:SerializedName("is_reviewed")
	val isReviewed: Boolean,

	@field:SerializedName("movie")
	val movie: Movie,

	@field:SerializedName("reviews")
	val reviews: List<ReviewsItem>,

	@field:SerializedName("error")
	val error: Boolean
)

data class Movie(

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("title")
	val title: String
)

data class ReviewsItem(

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
	val username: String,

	@field:SerializedName("profile_picture")
	val profilePicture: String
)
