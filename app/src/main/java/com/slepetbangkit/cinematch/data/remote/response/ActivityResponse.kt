package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class ActivityResponse(

	@field:SerializedName("activities")
	val activities: List<ActivitiesItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class ActivitiesItem(

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("followed_username")
	val followedUsername: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("movie_tmdb_id")
	val movieTmdbId: Any? = null,

	@field:SerializedName("username")
	val username: String? = null
)
