package com.entin.worldnews.data.datasource.remote

import com.entin.network.api.CATEGORY_GENERAL
import com.entin.network.api.CATEGORY_HEALTH
import com.entin.network.api.CATEGORY_SPORTS
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


    override suspend fun getNews2(country: Country): List<ArticleJson> {
        val general = getGeneralNewsByCountry2(country).onEach { it.category = CATEGORY_GENERAL }
        val health = getHealthNewsByCountry2(country).onEach { it.category = CATEGORY_HEALTH }
        val sports = getSportsNewsByCountry2(country).onEach { it.category = CATEGORY_SPORTS }

        return mutableListOf<ArticleJson>().apply {
            addAll(general)
            addAll(health)
            addAll(sports)
            // Let mix all topics of news
            shuffle()
        }
    }

    private suspend fun getGeneralNewsByCountry2(country: Country) =
        apiService.getNewsByCountryGeneral(country = country.countryName).articles

    private suspend fun getHealthNewsByCountry2(country: Country) =
        apiService.getNewsByCountryHealth(country = country.countryName).articles

    private suspend fun getSportsNewsByCountry2(country: Country) =
        apiService.getNewsByCountrySports(country = country.countryName).articles
}