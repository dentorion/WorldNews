package com.entin.worldnews.data.datasource.local

import com.entin.db.dao.NewsDAO
import com.entin.db.entity.ArticleRoom
import com.entin.worldnews.domain.model.Country
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val newsDAO: NewsDAO
) : LocalDataSource {

    override fun getNews(country: Country): Flow<List<ArticleRoom>> =
        newsDAO.getNews(country = country.countryName)

    override fun getFavouriteNews(): Flow<List<ArticleRoom>> =
        newsDAO.getFavouriteNews()

    override suspend fun saveNewsToDb(articles: List<ArticleRoom>) =
        newsDAO.saveNews(articles)

    override suspend fun saveSearchedAndOpenedArticle(article: ArticleRoom) =
        newsDAO.saveSearchedAndOpenedArticle(article)

    override suspend fun setArticleAsShown(url: String) =
        newsDAO.setArticleShown(url = url)

    override suspend fun changeFavouriteStatusArticle(url: String) =
        newsDAO.changeFavouriteStatusArticle(url = url)

    override suspend fun getFavouriteStatusArticle(url: String): Flow<Boolean> =
        newsDAO.getFavouriteStatusArticle(url = url)

    override suspend fun deleteNewsByCountry(country: Country) =
        newsDAO.deleteNews(country.countryName)
}