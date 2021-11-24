package com.entin.worldnews.data.datasource.remote

import com.entin.network.api.NewsApiService
import com.entin.network.model.ArticleJson
import com.entin.worldnews.domain.model.Country
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: NewsApiService
) : RemoteDataSource {

    override suspend fun getNews(country: Country): List<ArticleJson> =
        apiService.getNewsByCountryGeneral(country = country.countryName).articles

    override suspend fun getSearchNews(query: String): List<ArticleJson> =
        apiService.getSearchedNews(query = query).articles
}