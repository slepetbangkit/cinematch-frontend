package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserSearchResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("users")
	val users: List<UsersItem?>? = null
)

data class UsersItem(

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
