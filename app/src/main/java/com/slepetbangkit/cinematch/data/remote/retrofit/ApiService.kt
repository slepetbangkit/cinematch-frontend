package com.slepetbangkit.cinematch.data.remote.retrofit

import com.slepetbangkit.cinematch.data.remote.request.UpdatePlaylistRequest
import com.slepetbangkit.cinematch.data.remote.response.ActivityResponse
import com.slepetbangkit.cinematch.data.remote.response.AddReviewResponse
import com.slepetbangkit.cinematch.data.remote.response.FollowListResponse
import com.slepetbangkit.cinematch.data.remote.response.HomeResponse
import com.slepetbangkit.cinematch.data.remote.response.MessageResponse
import com.slepetbangkit.cinematch.data.remote.response.LoginResponse
import com.slepetbangkit.cinematch.data.remote.response.MovieDetailsResponse
import com.slepetbangkit.cinematch.data.remote.response.MovieReviewsResponse
import com.slepetbangkit.cinematch.data.remote.response.MovieSearchResponseItem
import com.slepetbangkit.cinematch.data.remote.response.PlaylistsItem
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.remote.response.RefreshResponse
import com.slepetbangkit.cinematch.data.remote.response.RegisterResponse
import com.slepetbangkit.cinematch.data.remote.response.ReviewDetailsResponse
import com.slepetbangkit.cinematch.data.remote.response.UserSearchResponse
import retrofit2.http.Body
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("user/register/")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("bio") bio: String = ""
    ): RegisterResponse

    @FormUrlEncoded
    @POST("user/token/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("user/token/refresh/")
    suspend fun refresh(
        @Field("refresh") refreshToken: String
    ): RefreshResponse

    @GET("/movies/home/")
    suspend fun getHomeMovies(
        @Header("Authorization") token: String
    ): HomeResponse

    @GET("movies/")
    suspend fun searchMovies(
        @Header("Authorization") token: String,
        @Query("search") query: String
    ): List<MovieSearchResponseItem>

    @GET("user/profile/search")
    suspend fun searchUser(
        @Header("Authorization") token: String,
        @Query("query") query: String
    ): UserSearchResponse

    @GET("movies/details/{tmdb_id}/")
    suspend fun getMovieDetails(
        @Header("Authorization") token: String,
        @Path("tmdb_id") tmdbId: Int
    ): MovieDetailsResponse

    @GET("user/activities/")
    suspend fun getActivities(
        @Header("Authorization") token: String
    ): ActivityResponse

    @GET("movies/details/{tmdb_id}/review/")
    suspend fun getMovieReviews(
        @Header("Authorization") token: String,
        @Path("tmdb_id") tmdbId: Int
    ): MovieReviewsResponse

    @GET("movies/review/{review_id}/")
    suspend fun getReviewDetailsById(
        @Header("Authorization") token: String,
        @Path("review_id") reviewId: String
    ): ReviewDetailsResponse

    @FormUrlEncoded
    @POST("movies/details/{tmdb_id}/review/")
    suspend fun addReview(
        @Header("Authorization") token: String,
        @Path("tmdb_id") tmdbId: Int,
        @Field("description") description: String
    ): AddReviewResponse

    @GET("user/profile/{username}/")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): ProfileResponse

    @Multipart
    @PATCH("user/profile/{username}/")
    suspend fun updateSelfProfile(
        @Header("Authorization") token: String,
        @Path("username") username: String,
        @Part("bio") newBio: RequestBody?,
        @Part image: MultipartBody.Part?
    ): MessageResponse

    @GET("user/profile/{username}/following/")
    suspend fun getFollowList(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): FollowListResponse

    @POST("user/profile/{username}/following/")
    suspend fun followUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): MessageResponse

    @DELETE("user/profile/{username}/following/")
    suspend fun unfollowUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): MessageResponse

    @GET("movies/playlists/{list_id}/")
    suspend fun getMovieListDetails(
        @Header("Authorization") token: String,
        @Path("list_id") listId: String
    ): PlaylistsItem

    @FormUrlEncoded
    @POST("movies/playlists/")
    suspend fun createMovieList(
        @Header("Authorization") token: String,
        @Field("title") title: String
    ): PlaylistsItem

    @PATCH("movies/playlists/{list_id}/")
    suspend fun updatePlaylist(
        @Header("Authorization") token: String,
        @Path("list_id") listId: String,
        @Body request: UpdatePlaylistRequest
    ): PlaylistsItem

    @DELETE("movies/playlists/{list_id}/")
    suspend fun deleteListById(
        @Header("Authorization") token: String,
        @Path("list_id") listId: String
    ): MessageResponse

    @GET("movies/playlists/")
    suspend fun getListUser(
        @Header("Authorization") token: String,
    ): List<PlaylistsItem>

    @POST("movies/playlists/blend/{user_id}/")
    suspend fun blendLists(
        @Header("Authorization") token: String,
        @Path("user_id") userId: String
    ): MessageResponse
}