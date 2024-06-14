package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class FollowListResponse(

	@field:SerializedName("followers")
	val followers: List<FollowListItem?>? = null,

	@field:SerializedName("following_count")
	val followingCount: Int? = null,

	@field:SerializedName("followings")
	val followings: List<FollowListItem?>? = null,

	@field:SerializedName("follower_count")
	val followerCount: Int? = null
)

data class FollowListItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)