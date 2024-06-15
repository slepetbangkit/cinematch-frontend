package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class ActivityResponse(

	@field:SerializedName("activities")
	val activities: List<ActivitiesItem?>,

	@field:SerializedName("error")
	val error: Boolean
)

data class ActivitiesItem(

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("followed_username")
	val followedUsername: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("movie_tmdb_id")
	val movieTmdbId: Any,

	@field:SerializedName("username")
	val username: String
)
