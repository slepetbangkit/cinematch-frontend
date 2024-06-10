package com.slepetbangkit.cinematch.data.remote.retrofit

import com.slepetbangkit.cinematch.data.remote.response.LoginResponse
import com.slepetbangkit.cinematch.data.remote.response.ProfileResponse
import com.slepetbangkit.cinematch.data.remote.response.RegisterResponse
import com.slepetbangkit.cinematch.data.remote.response.MovieSearchResponse
import com.slepetbangkit.cinematch.data.remote.response.SearchResponseItem
import com.slepetbangkit.cinematch.data.remote.response.UserSearchResponse
import com.slepetbangkit.cinematch.data.remote.response.UsersItem
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
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

    @GET("user/profile/{username}/")
    fun getProfile(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<ProfileResponse>

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
}