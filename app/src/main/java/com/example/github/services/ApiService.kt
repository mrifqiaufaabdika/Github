package com.example.github.services

import com.example.github.SearchResponse
import com.example.github.responses.DetailUserResponse
import com.example.github.responses.FollowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("search/users")
    fun getUser(
        @Header("Authorization") token: String,
        @Query("q") q:String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ):Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ):Call<List<FollowResponse>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ):Call<List<FollowResponse>>

}