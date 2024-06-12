package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(

	@field:SerializedName("trailer_link")
	val trailerLink: String? = null,

	@field:SerializedName("tmdb_id")
	val tmdbId: Int? = null,

	@field:SerializedName("cast")
	val cast: List<CastItem?>? = null,

	@field:SerializedName("similar_movies")
	val similarMovies: List<SimilarMoviesItem?>? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("director")
	val director: String? = null,

	@field:SerializedName("rating")
	val rating: Any? = null,

	@field:SerializedName("poster_url")
	val posterUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("crew")
	val crew: List<CrewItem?>? = null
)

data class CastItem(

	@field:SerializedName("character")
	val character: String? = null,

	@field:SerializedName("profile_url")
	val profileUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class SimilarMoviesItem(

	@field:SerializedName("tmdb_id")
	val tmdbId: Int? = null,

	@field:SerializedName("poster_url")
	val posterUrl: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class CrewItem(

	@field:SerializedName("profile_url")
	val profileUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("job")
	val job: String? = null
)
