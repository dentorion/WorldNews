package com.example.network.api

import com.example.network.BuildConfig
import com.example.network.entity.ApiResponseNewsTop
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    // GET NEWS

    @GET("v2/top-headlines")
    suspend fun getNewsByCountry(
        @Query("country") country: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 100,
        @Query("category") category: String = "general",
        @Query("apiKey") apiKey: String = BuildConfig.MY_KEY
    ): ApiResponseNewsTop

    // SEARCH

    @GET("v2/everything")
    suspend fun getSearchedNews(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 100,
        @Query("apiKey") apiKey: String = BuildConfig.MY_KEY
    ): ApiResponseNewsTop
}