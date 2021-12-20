package com.entin.worldnews.data.repository

import com.entin.db.entity.ArticleRoom
import com.entin.extension.handleRequest
import com.entin.worldnews.data.datasource.local.LocalDataSource
import com.entin.worldnews.data.datasource.local.sharedpref.CacheNewsController
import com.entin.worldnews.data.datasource.remote.RemoteDataSource
import com.entin.worldnews.data.extension.toDbModel
import com.entin.worldnews.data.extension.toDomainModel
import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.domain.model.UseCaseResult
import com.entin.worldnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Downloading news only once in 2 hours or manually forced download
 * Time of last downloading is saved in Preferences by extension
 */

class NewsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val cacheSettingsController: CacheNewsController,
) : NewsRepository {

    // All getting news functions

    /**
     * Get news
     * If cache controller allow -> download new news from server,
     * if not -> get news from database
     */
    override suspend fun getNews(country: Country): Flow<UseCaseResult> = flow {
        if (cacheSettingsController.isAllowDownloadNews(country)) {
            downloadNews(country).collect { listNews ->
                emit(listNews)
            }
        } else {
            getNewsFromDb(country).collect { listNews ->
                emit(listNews)
            }
        }
    }

    /**
     * Forced downloading news by country
     * Update preferences by country and last download time
     */
    override suspend fun forcedNewsDownload(country: Country): Flow<UseCaseResult> = flow {
        downloadNews(country).collect { listNews ->
            emit(listNews)
        }
    }

    /**
     * Get news from database in offline mode
     */
    override suspend fun getOfflineNews(country: Country): Flow<UseCaseResult> = flow {
        getNewsFromDb(country).collect { listNews ->
            emit(listNews)
        }
    }

    /**
     * List of favourites news from DB
     */
    override suspend fun getFavouriteNews(): Flow<UseCaseResult> = flow {
        localDataSource.getFavouriteNews().collect { list ->
            if (list.isEmpty()) {
                emit(UseCaseResult.Empty)
            } else {
                emit(UseCaseResult.Success(list.map { it.toDomainModel() }))
            }
        }
    }

    /**
     *  Search news WITHOUT saving to DB
     */
    override suspend fun getSearchNews(query: String): Flow<UseCaseResult> = flow {
        query.isNotEmpty().let {
            getSearchNewsSafeRequest(query).also { result ->
                result.onSuccess { list ->
                    if (list.isEmpty()) {
                        emit(UseCaseResult.Empty)
                    } else {
                        emit(UseCaseResult.Success(list))
                    }
                }.onFailure { exception ->
                    emit(UseCaseResult.Error(exception))
                }
            }
        }
    }

    // Util functions for working with Article and Delete all saved news

    /**
     * Save Searched and Opened article
     */
    override suspend fun saveSearchedAndOpenedArticle(article: Article) {
        val articleRoom = article.toDbModel()
        localDataSource.saveSearchedAndOpenedArticle(articleRoom)
    }

    /**
     * Set opened article as shown (eye icon)
     */
    override suspend fun setArticleShown(url: String) =
        localDataSource.setArticleAsShown(url = url)

    /**
     * Change favourite state of article
     */
    override suspend fun changeFavouriteArticle(url: String) =
        localDataSource.changeFavouriteStatusArticle(url = url)

    /**
     * Get favourite state of article
     */
    override suspend fun getFavouriteStatusArticle(url: String): Flow<Boolean> =
        localDataSource.getFavouriteStatusArticle(url = url)

    /**
     * Delete all news by country
     */
    override suspend fun deleteNewsByCountry(country: Country) {
        localDataSource.deleteNewsByCountry(country)
        cacheSettingsController.clearLastTimeDownload(country)
    }

    // Private functions

    /**
     * Used by public functions:
     *  - getNews()
     *  - forcedNewsDownload()
     */
    private suspend fun downloadNews(country: Country) = flow {
        downloadNewsFromServerApi(country).collect { result ->
            result.onSuccess { list ->
                if (list.isEmpty()) {
                    emit(UseCaseResult.Empty)
                } else {
                    cacheSettingsController.clearLastTimeDownload(country)
                    cacheSettingsController.setLastTimeDownload(country)
                    saveNewsDb(list)
                    getNewsFromDb(country = country).collect { newsDb ->
                        emit(newsDb)
                    }
                }
            }.onFailure { exception ->
                emit(UseCaseResult.Error(exception))
            }
        }
    }

    /**
     * Safe request to the server to get news by country
     */
    private suspend fun downloadNewsFromServerApi(country: Country): Flow<Result<List<ArticleRoom>>> =
        flow {
            emit(handleRequest {
                remoteDataSource.getNews(country = country)
                    .map { it.toDbModel() }
                    .also { list -> list.map { it.country = country.countryName } }
            })
        }

    /**
     * Safe request to the server to get news by search query
     */
    private suspend fun getSearchNewsSafeRequest(query: String): Result<List<Article>> {
        return handleRequest {
            remoteDataSource.getSearchNews(query).map { it.toDomainModel() }
        }
    }

    /**
     * Get news from database and convert news to the domain model
     */
    private fun getNewsFromDb(country: Country) = flow {
        localDataSource.getNews(country).collect { list ->
            if (list.isNotEmpty()) {
                emit(UseCaseResult.Success(list.map { it.toDomainModel() }))
            } else {
                emit(UseCaseResult.Empty)
            }
        }
    }

    /**
     * Save news from server to database
     */
    private suspend fun saveNewsDb(articles: List<ArticleRoom>) =
        localDataSource.saveNewsToDb(articles = articles)
}