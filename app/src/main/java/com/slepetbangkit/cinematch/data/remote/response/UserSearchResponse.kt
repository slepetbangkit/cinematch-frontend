package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserSearchResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("users")
	val users: List<UsersItem>
)

data class UsersItem(

	@field:SerializedName("profile_picture")
	val profilePicture: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("username")
	val username: String
)
