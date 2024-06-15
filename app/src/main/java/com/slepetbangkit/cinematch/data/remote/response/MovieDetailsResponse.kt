package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(

	@field:SerializedName("trailer_link")
	val trailerLink: String? = null,

	@field:SerializedName("tmdb_id")
	val tmdbId: Int? = null,

	@field:SerializedName("backdrop_url")
	val backdropUrl: String? = null,

	@field:SerializedName("languages")
	val languages: String? = null,

	@field:SerializedName("director")
	val director: String? = null,

	@field:SerializedName("overall_sentiment")
	val overallSentiment: String? = null,

	@field:SerializedName("rating")
	val rating: Any? = null,

	@field:SerializedName("poster_url")
	val posterUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("runtime")
	val runtime: Int? = null,

	@field:SerializedName("in_playlists")
	val inPlaylists: List<Any?>? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("crew")
	val crew: List<CrewItem?>? = null,

	@field:SerializedName("cast")
	val cast: List<CastItem?>? = null,

	@field:SerializedName("similar_movies")
	val similarMovies: List<SimilarMoviesItem?>? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("genres")
	val genres: List<String?>? = null,

	@field:SerializedName("origin_countries")
	val originCountries: List<String?>? = null,

	@field:SerializedName("is_liked")
	val isLiked: Boolean? = null
)

data class CastItem(

	@field:SerializedName("character")
	val character: String? = null,

	@field:SerializedName("profile_url")
	val profileUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class CrewItem(

	@field:SerializedName("profile_url")
	val profileUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("job")
	val job: String? = null
)

data class SimilarMoviesItem(

	@field:SerializedName("tmdb_id")
	val tmdbId: Int? = null,

	@field:SerializedName("poster_url")
	val posterUrl: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
