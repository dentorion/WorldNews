package com.entin.worldnews.domain.repository

import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(country: Country): Flow<List<Article>>

    suspend fun forcedNewsDownload(country: Country): Flow<List<Article>>

    suspend fun getFavouriteNews(): Flow<List<Article>>

    suspend fun getSearchNews(query: String): Flow<List<Article>>

    suspend fun saveSearchedAndOpenedArticle(article: Article)

    suspend fun setArticleShown(url: String)

    suspend fun changeFavouriteArticle(url: String)

    suspend fun getFavouriteStatusArticle(url: String): Flow<Boolean>

    suspend fun deleteNewsByCountry(country: Country)

    suspend fun checkLastTimeDownload(country: Country): Boolean
}