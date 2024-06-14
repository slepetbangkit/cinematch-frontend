package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(

	@field:SerializedName("SearchResponse")
	val searchResponse: List<SearchResponseItem?>? = null
)

data class SearchResponseItem(

	@field:SerializedName("tmdb_id")
	val tmdbId: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("poster_url")
	val posterUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("director")
	val director: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("rating")
	val rating: Any? = null,
)
