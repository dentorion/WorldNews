package com.entin.network.api

import com.entin.network.model.ApiResponseNewsTop
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    /**
     * GET NEWS
     */

    @GET("v2/top-headlines")
    suspend fun getNewsByCountryGeneral(
        @Query(COUNTRY) country: String,
        @Query(PAGE) page: Int = 1,
        @Query(PAGE_SIZE) pageSize: Int = 100,
        @Query(CATEGORY) category: String = CATEGORY_GENERAL,
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

private const val COUNTRY = "country"
private const val PAGE = "page"
private const val PAGE_SIZE = "pageSize"
private const val CATEGORY = "category"
private const val CATEGORY_GENERAL = "general"
private const val QUERY = "q"