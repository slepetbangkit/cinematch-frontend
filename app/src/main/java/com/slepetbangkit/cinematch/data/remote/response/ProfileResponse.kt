package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("is_following_user")
	val isFollowingUser: Boolean,

	@field:SerializedName("following_count")
	val followingCount: Int,

	@field:SerializedName("is_followed")
	val isFollowed: Boolean,

	@field:SerializedName("playlists")
	val playlists: List<PlaylistsItem>,

	@field:SerializedName("bio")
	val bio: String,

	@field:SerializedName("profile_picture")
	val profilePicture: Any? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("follower_count")
	val followerCount: Int,

	@field:SerializedName("username")
	val username: String
)

data class MoviesItem(

	@field:SerializedName("tmdb_id")
	val tmdbId: Int,

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("director")
	val director: String,

	@field:SerializedName("rating")
	val rating: Any,

	@field:SerializedName("poster_url")
	val posterUrl: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("review_count")
	val reviewCount: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String
)

data class PlaylistsItem(

	@field:SerializedName("movies")
	val movies: List<MoviesItem>,

	@field:SerializedName("is_favorite")
	val isFavorite: Boolean,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("user")
	val user: String,

	@field:SerializedName("username")
	val username: String
)
