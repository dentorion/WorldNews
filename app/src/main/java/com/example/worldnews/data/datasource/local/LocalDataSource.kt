package com.example.worldnews.data.datasource.local

import com.example.db.entity.ArticleRoom
import com.example.worldnews.domain.entity.Country
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getNews(country: Country): Flow<List<ArticleRoom>>

    fun getFavouriteNews(): Flow<List<ArticleRoom>>

    suspend fun saveNewsToDb(articles: List<ArticleRoom>)

    suspend fun saveSearchedAndOpenedArticle(article: ArticleRoom)

    suspend fun setArticleAsShown(url: String)

    suspend fun changeFavouriteStatusArticle(url: String)

    suspend fun getFavouriteStatusArticle(url: String): Flow<Boolean>

    suspend fun deleteNewsByCountry(country: Country)

}