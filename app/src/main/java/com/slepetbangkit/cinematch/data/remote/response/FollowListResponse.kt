package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class FollowListResponse(

	@field:SerializedName("followers")
	val followers: List<FollowListItem?>,

	@field:SerializedName("following_count")
	val followingCount: Int,

	@field:SerializedName("followings")
	val followings: List<FollowListItem?>,

	@field:SerializedName("follower_count")
	val followerCount: Int
)

data class FollowListItem(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null
)
