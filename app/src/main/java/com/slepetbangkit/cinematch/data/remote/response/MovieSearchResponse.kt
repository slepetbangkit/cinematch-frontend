package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieSearchResponseItem(

	@field:SerializedName("tmdb_id")
	val tmdbId: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("poster_url")
	val posterUrl: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("director")
	val director: String,

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("rating")
	val rating: Any,
)
