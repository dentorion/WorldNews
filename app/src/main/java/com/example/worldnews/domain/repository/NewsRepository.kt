package com.example.worldnews.domain.repository

import com.example.worldnews.domain.entity.Article
import com.example.worldnews.domain.entity.Country
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(country: Country): Flow<List<Article>>

    suspend fun forcedNewsDownload(country: Country): Flow<List<Article>>

    suspend fun getFavouriteNews(): Flow<List<Article>>

    suspend fun getSearchNews(query: String): List<Article>

    suspend fun saveSearchedAndOpenedArticle(article: Article)

    suspend fun setArticleShown(url: String)

    suspend fun changeFavouriteArticle(url: String)

    suspend fun getFavouriteStatusArticle(url: String): Flow<Boolean>

    suspend fun deleteNewsByCountry(country: Country)
}