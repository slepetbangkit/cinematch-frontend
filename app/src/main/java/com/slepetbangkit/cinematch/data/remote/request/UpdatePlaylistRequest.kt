package com.slepetbangkit.cinematch.data.remote.request

data class UpdatePlaylistRequest(
    val delete_movie_tmdb_id: List<Int>? = null,
    val new_movie_tmdb_id: List<Int>? = null,
    val title: String? = null,
    val description: String? = null,
    val is_favorite: Boolean? = null
)
