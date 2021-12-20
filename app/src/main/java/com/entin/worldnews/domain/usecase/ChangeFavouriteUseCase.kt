package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.repository.NewsRepository
import javax.inject.Inject

/**
 * UseCase for changing boolean status of Article in favourite
 * Business Logic placed here
 */

class ChangeFavouriteUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(url: String) {
        repository.changeFavouriteArticle(url = url)
    }
}