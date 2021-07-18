package com.example.worldnews.data.datasource.remote

import com.example.network.entity.ArticleJson
import com.example.worldnews.domain.entity.Country

interface RemoteDataSource {

    suspend fun getNews(country: Country): List<ArticleJson>

    suspend fun getSearchNews(query: String): List<ArticleJson>
}