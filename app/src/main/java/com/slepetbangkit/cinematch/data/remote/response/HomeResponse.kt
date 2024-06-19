package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class HomeResponse(

	@field:SerializedName("data")
	val data: HomeData,

	@field:SerializedName("error")
	val error: Boolean
)

data class HomeData(

	@field:SerializedName("top_rated")
	val topRated: List<TopRatedItem>,

	@field:SerializedName("verdict")
	val verdict: List<VerdictItem>,

	@field:SerializedName("friends")
	val friends: List<FriendsItem>,

	@field:SerializedName("recommended")
	val recommended: List<RecommendedItem>
)

data class VerdictItem(

	@field:SerializedName("review_id")
	val reviewId: String,

	@field:SerializedName("tmdb_id")
	val tmdbId: Int,

	@field:SerializedName("poster_url")
	val posterUrl: String,

	@field:SerializedName("profile_picture")
	val profilePicture: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,
)

data class RecommendedItem(

	@field:SerializedName("tmdb_id")
	val tmdbId: Int,

	@field:SerializedName("poster_url")
	val posterUrl: String,

	@field:SerializedName("title")
	val title: String
)

data class FriendsItem(

	@field:SerializedName("tmdb_id")
	val tmdbId: Int,

	@field:SerializedName("poster_url")
	val posterUrl: String,

	@field:SerializedName("profile_picture")
	val profilePicture: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("username")
	val username: String
)

data class TopRatedItem(

	@field:SerializedName("tmdb_id")
	val tmdbId: Int,

	@field:SerializedName("poster_url")
	val posterUrl: String,

	@field:SerializedName("title")
	val title: String
)
