package com.entin.worldnews.domain.repository

import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.domain.model.UseCaseResult
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(country: Country): Flow<UseCaseResult>

    suspend fun forcedNewsDownload(country: Country): Flow<UseCaseResult>

    suspend fun getFavouriteNews(): Flow<UseCaseResult>

    suspend fun getSearchNews(query: String): Flow<UseCaseResult>

    suspend fun saveSearchedAndOpenedArticle(article: Article)

    suspend fun setArticleShown(url: String)

    suspend fun changeFavouriteArticle(url: String)

    suspend fun getFavouriteStatusArticle(url: String): Flow<Boolean>

    suspend fun deleteNewsByCountry(country: Country)

    suspend fun getOfflineNews(country: Country): Flow<UseCaseResult>
}