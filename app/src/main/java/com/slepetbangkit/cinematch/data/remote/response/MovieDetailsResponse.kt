package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(

	@field:SerializedName("trailer_link")
	val trailerLink: String?,

	@field:SerializedName("tmdb_id")
	val tmdbId: Int,

	@field:SerializedName("backdrop_url")
	val backdropUrl: String,

	@field:SerializedName("languages")
	val languages: String,

	@field:SerializedName("director")
	val director: String,

	@field:SerializedName("overall_sentiment")
	val overallSentiment: String,

	@field:SerializedName("rating")
	val rating: Any,

	@field:SerializedName("poster_url")
	val posterUrl: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("runtime")
	val runtime: Int,

	@field:SerializedName("in_playlists")
	val inPlaylists: List<PlaylistItem>,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("crew")
	val crew: List<CrewItem>,

	@field:SerializedName("cast")
	val cast: List<CastItem>,

	@field:SerializedName("similar_movies")
	val similarMovies: List<SimilarMoviesItem>,

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("genres")
	val genres: List<String>,

	@field:SerializedName("origin_countries")
	val originCountries: List<String>,

	@field:SerializedName("is_liked")
	val isLiked: Boolean
)

data class SimilarMoviesItem(

	@field:SerializedName("tmdb_id")
	val tmdbId: Int,

	@field:SerializedName("poster_url")
	val posterUrl: String,

	@field:SerializedName("title")
	val title: String
)

data class CastItem(

	@field:SerializedName("character")
	val character: String,

	@field:SerializedName("profile_url")
	val profileUrl: String,

	@field:SerializedName("name")
	val name: String
)

data class CrewItem(

	@field:SerializedName("profile_url")
	val profileUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("job")
	val job: String
)

data class PlaylistItem(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("poster_url")
	val posterUrl: String
)
