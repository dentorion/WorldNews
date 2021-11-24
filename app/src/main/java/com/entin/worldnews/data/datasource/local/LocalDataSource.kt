package com.entin.worldnews.data.datasource.local

import com.entin.db.entity.ArticleRoomModel
import com.entin.worldnews.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getNews(country: Country): Flow<List<ArticleRoomModel>>

    fun getFavouriteNews(): Flow<List<ArticleRoomModel>>

    suspend fun saveNewsToDb(articleModels: List<ArticleRoomModel>)

    suspend fun saveSearchedAndOpenedArticle(articleModel: ArticleRoomModel)

    suspend fun setArticleAsShown(url: String)

    suspend fun changeFavouriteStatusArticle(url: String)

    suspend fun getFavouriteStatusArticle(url: String): Flow<Boolean>

    suspend fun deleteNewsByCountry(country: Country)

}