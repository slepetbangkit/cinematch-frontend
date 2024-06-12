package com.slepetbangkit.cinematch.data.remote.retrofit

import com.slepetbangkit.cinematch.data.remote.response.ActivityResponse
import com.slepetbangkit.cinematch.data.remote.response.FollowListResponse
import com.slepetbangkit.cinematch.data.remote.response.MessageResponse
import com.slepetbangkit.cinematch.data.remote.response.LoginResponse
import com.slepetbangkit.cinematch.data.remote.response.MovieDetailResponse
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.remote.response.RegisterResponse
import com.slepetbangkit.cinematch.data.remote.response.SearchResponseItem
import com.slepetbangkit.cinematch.data.remote.response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("user/register/")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("user/token/")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("movies/")
    fun searchMovies(
        @Header("Authorization") token: String,
        @Query("search") query: String
    ): Call<List<SearchResponseItem>>

    @GET("user/profile/search")
    fun searchUser(
        @Header("Authorization") token: String,
        @Query("query") query: String
    ): Call<UserSearchResponse>

    @GET("movies/details/{tmdb_id}/")
    fun getMovieDetail(
        @Header("Authorization") token: String,
        @Path("tmdb_id") tmdbId: Int
    ): Call<MovieDetailResponse>

    @GET("user/activities/")
    fun getActivities(
        @Header("Authorization") token: String
    ): Call<ActivityResponse>

    @GET("user/profile/{username}/")
    fun getProfile(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @PATCH("user/profile/{username}/")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Path("username") username: String,
        @Field("username") newUsername: String,
        @Field("bio") newBio: String
    ): Call<MessageResponse>

    @POST("user/profile/{username}/following/")
    fun followUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<MessageResponse>

    @DELETE("user/profile/{username}/following/")
    fun unfollowUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<MessageResponse>

    @GET("user/profile/{username}/following/")
    fun getFollowing(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<FollowListResponse>
}