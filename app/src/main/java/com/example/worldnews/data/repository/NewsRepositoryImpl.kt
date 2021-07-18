package com.example.worldnews.data.repository

import com.example.db.entity.ArticleRoom
import com.example.worldnews.data.datasource.local.LocalDataSource
import com.example.worldnews.data.datasource.remote.RemoteDataSource
import com.example.worldnews.data.extension.toDbModel
import com.example.worldnews.data.extension.toDomainModel
import com.example.worldnews.domain.entity.Article
import com.example.worldnews.domain.entity.Country
import com.example.worldnews.domain.repository.NewsRepository
import com.example.worldnews.presentation.util.NewsSharedPreferences
import com.example.worldnews.presentation.util.TIME_2HOURS_DOWNLOAD_PAUSE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Downloading news only once in 2 hours or manually forced download
 * Time of last downloading is saved in Preferences by extension
 */

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferences: NewsSharedPreferences
) : NewsRepository {

    /**
     * Check news by country were downloaded
     * Check last lime news downloaded
     * return from SERVER->DB or DB
     */
    override suspend fun getNews(country: Country): Flow<List<Article>> {
        return if (sharedPreferences.wasDownloaded(country)) {
            if (checkTimeDifference(sharedPreferences.getLastTimeDownloaded(country))) {
                downloadNews(country)
            } else {
                getNewsFromDb(country)
            }
        } else {
            downloadNews(country)
        }
    }

    /**
     * Forced downloading news by country
     * Update preferences by country and last download time
     */
    override suspend fun forcedNewsDownload(country: Country): Flow<List<Article>> {
        val response = downloadNews(country)
        sharedPreferences.clearLastDownload(country)
        sharedPreferences.addCurrentTimeByCountry(country)
        return response
    }

    /**
     * List of favourites news from DB
     */
    override suspend fun getFavouriteNews(): Flow<List<Article>> =
        localDataSource.getFavouriteNews().map { it ->
            it.map { it.toDomainModel() }
        }

    /**
     *  Search news WITHOUT saving to DB
     */
    override suspend fun getSearchNews(query: String): List<Article> {
        return if (query.isNotEmpty()) {
            remoteDataSource.getSearchNews(query).map { it.toDomainModel() }
        } else {
            listOf()
        }
    }

    /**
     * Save Searched and Opened article
     */
    override suspend fun saveSearchedAndOpenedArticle(article: Article) {
        val articleRoom = ArticleRoom(
            url = article.url,
            title = article.title,
            author = article.author,
            content = article.content,
            description = article.description,
            publishedAt = article.publishedAt,
            urlToImage = article.urlToImage
        )
        localDataSource.saveSearchedAndOpenedArticle(articleRoom)
    }

    override suspend fun setArticleShown(url: String) =
        localDataSource.setArticleAsShown(url = url)

    override suspend fun changeFavouriteArticle(url: String) =
        localDataSource.changeFavouriteStatusArticle(url = url)

    override suspend fun getFavouriteStatusArticle(url: String): Flow<Boolean> =
        localDataSource.getFavouriteStatusArticle(url = url)

    override suspend fun deleteNewsByCountry(country: Country) {
        localDataSource.deleteNewsByCountry(country)
        sharedPreferences.clearLastDownload(country)
    }

    // Private functions

    private suspend fun downloadNews(country: Country): Flow<List<Article>> {
        val apiAnswer = getNewsApiAndConvertToDbModel(country)
        return if (apiAnswer.isNotEmpty()) {
            sharedPreferences.addCurrentTimeByCountry(country)
            saveNewsDb(apiAnswer)
            getNewsFromDb(country)
        } else {
            getNewsFromDb(country)
        }
    }

    private fun checkTimeDifference(longTime: Long): Boolean {
        if (System.currentTimeMillis().minus(longTime) < TIME_2HOURS_DOWNLOAD_PAUSE) {
            return false
        }
        return true
    }

    private suspend fun getNewsApiAndConvertToDbModel(country: Country): List<ArticleRoom> {
        val response = remoteDataSource.getNews(country = country).map { it.toDbModel() }
        response.map { it.country = country.countryName }
        return response
    }

    private fun getNewsFromDb(country: Country): Flow<List<Article>> =
        localDataSource.getNews(country = country).map { it ->
            it.map { it.toDomainModel() }
        }

    private suspend fun saveNewsDb(articles: List<ArticleRoom>) =
        localDataSource.saveNewsToDb(articles = articles)

}