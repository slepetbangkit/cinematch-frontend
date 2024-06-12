package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("following_count")
	val followingCount: Int? = null,

	@field:SerializedName("is_followed")
	val isFollowed: Boolean? = null,

	@field:SerializedName("playlists")
	val playlists: List<Any?>? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("is_self")
	val isSelf: Boolean? = null,

	@field:SerializedName("follower_count")
	val followerCount: Int? = null,

	@field:SerializedName("username")
	val username: String? = null
)
