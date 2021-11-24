package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavouriteNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend fun execute(): Flow<List<Article>> =
        repository.getFavouriteNews()

    suspend fun deleteFromFavouriteNews(url: String) =
        repository.changeFavouriteArticle(url)
    
}