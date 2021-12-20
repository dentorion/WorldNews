package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UseCase for setting Article as watched in database
 * Eye label
 *
 * Business Logic placed here
 */

@Singleton
class SetArticleWatchedUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(url: String) {
        repository.setArticleShown(url)
    }
}