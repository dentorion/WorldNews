package com.entin.network.api

import androidx.annotation.Keep
import com.entin.network.model.ApiResponseNewsTop
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    /**
     * GET NEWS "general" BY country
     */
    @GET("v2/top-headlines")
    suspend fun getNewsByCountryGeneral(
        @Query(COUNTRY) country: String,
        @Query(PAGE) page: Int = 1,
        @Query(PAGE_SIZE) pageSize: Int = 100,
        @Query(CATEGORY) category: String = CATEGORY_GENERAL,
    ): ApiResponseNewsTop

    /**
     * GET NEWS "health" BY country
     */
    @GET("v2/top-headlines")
    suspend fun getNewsByCountryHealth(
        @Query(COUNTRY) country: String,
        @Query(PAGE) page: Int = 1,
        @Query(PAGE_SIZE) pageSize: Int = 100,
        @Query(CATEGORY) category: String = CATEGORY_HEALTH,
    ): ApiResponseNewsTop

    /**
     * GET NEWS "sports" BY country
     */
    @GET("v2/top-headlines")
    suspend fun getNewsByCountrySports(
        @Query(COUNTRY) country: String,
        @Query(PAGE) page: Int = 1,
        @Query(PAGE_SIZE) pageSize: Int = 100,
        @Query(CATEGORY) category: String = CATEGORY_SPORTS,
    ): ApiResponseNewsTop

    /**
     * SEARCH
     */
    @GET("v2/everything")
    suspend fun getSearchedNews(
        @Query(QUERY) query: String,
        @Query(PAGE_SIZE) pageSize: Int = 100,
    ): ApiResponseNewsTop
}

// Part of query
@Keep
private const val COUNTRY = "country"
@Keep
private const val PAGE = "page"
@Keep
private const val PAGE_SIZE = "pageSize"
@Keep
private const val QUERY = "q"

/**
 * Server News API Topics of news
 */
@Keep
const val CATEGORY = "category"
@Keep
const val CATEGORY_GENERAL = "general"
@Keep
const val CATEGORY_HEALTH = "health"
@Keep
const val CATEGORY_SPORTS = "sports"