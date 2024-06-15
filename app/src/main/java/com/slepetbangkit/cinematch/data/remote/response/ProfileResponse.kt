package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("following_count")
	val followingCount: Int,

	@field:SerializedName("is_followed")
	val isFollowed: Boolean,

	@field:SerializedName("is_following_user")
	val isFollowingUser: Boolean,

	@field:SerializedName("playlists")
	val playlists: List<Any>,

	@field:SerializedName("bio")
	val bio: String,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("is_self")
	val isSelf: Boolean,

	@field:SerializedName("follower_count")
	val followerCount: Int,

	@field:SerializedName("username")
	val username: String
)
