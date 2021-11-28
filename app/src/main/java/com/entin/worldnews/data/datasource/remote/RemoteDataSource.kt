package com.entin.worldnews.data.datasource.remote

import com.entin.network.model.ArticleJson
import com.entin.worldnews.domain.model.Country

interface RemoteDataSource {

    suspend fun getNews(country: Country): List<ArticleJson>

    suspend fun getSearchNews(query: String): List<ArticleJson>

    suspend fun getNews2(country: Country): List<ArticleJson>
}