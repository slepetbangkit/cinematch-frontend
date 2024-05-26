package com.slepetbangkit.cinematch.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("is_superuser")
	val isSuperuser: Boolean? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("user_permissions")
	val userPermissions: List<Any?>? = null,

	@field:SerializedName("is_staff")
	val isStaff: Boolean? = null,

	@field:SerializedName("last_login")
	val lastLogin: Any? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("groups")
	val groups: List<Any?>? = null,

	@field:SerializedName("follower_count")
	val followerCount: Int? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("following_count")
	val followingCount: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("date_joined")
	val dateJoined: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
