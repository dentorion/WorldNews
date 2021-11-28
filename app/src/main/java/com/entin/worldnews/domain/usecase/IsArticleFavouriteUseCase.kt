package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase for getting information about is Article in favourite
 */

class IsArticleFavouriteUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(url: String): Flow<Boolean> =
        repository.getFavouriteStatusArticle(url = url)
}