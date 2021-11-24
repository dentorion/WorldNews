package com.entin.worldnews.data.datasource.local

import com.entin.db.dao.NewsDAO
import com.entin.db.entity.ArticleRoomModel
import com.entin.worldnews.domain.model.Country
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val newsDAO: NewsDAO
) : LocalDataSource {

    override fun getNews(country: Country): Flow<List<ArticleRoomModel>> =
        newsDAO.getNews(country = country.countryName)

    override fun getFavouriteNews(): Flow<List<ArticleRoomModel>> =
        newsDAO.getFavouriteNews()

    override suspend fun saveNewsToDb(articleModels: List<ArticleRoomModel>) =
        newsDAO.saveNews(articleModels)

    override suspend fun saveSearchedAndOpenedArticle(articleModel: ArticleRoomModel) =
        newsDAO.saveSearchedAndOpenedArticle(articleModel)

    override suspend fun setArticleAsShown(url: String) =
        newsDAO.setArticleShown(url = url)

    override suspend fun changeFavouriteStatusArticle(url: String) =
        newsDAO.changeFavouriteStatusArticle(url = url)

    override suspend fun getFavouriteStatusArticle(url: String): Flow<Boolean> =
        newsDAO.getFavouriteStatusArticle(url = url)

    override suspend fun deleteNewsByCountry(country: Country) =
        newsDAO.deleteNews(country.countryName)

}