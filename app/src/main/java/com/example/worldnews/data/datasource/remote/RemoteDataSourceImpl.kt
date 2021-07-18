package com.example.worldnews.data.datasource.remote

import com.example.network.api.NewsApiService
import com.example.network.entity.ArticleJson
import com.example.worldnews.domain.entity.Country
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: NewsApiService
) : RemoteDataSource {

    override suspend fun getNews(country: Country): List<ArticleJson> =
        apiService.getNewsByCountry(country = country.countryName).articles

    override suspend fun getSearchNews(query: String): List<ArticleJson> =
            apiService.getSearchedNews(query = query).articles
}