package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.domain.repository.NewsRepository
import javax.inject.Inject

/**
 * UseCase for clearing all news by country in database
 * Business Logic placed here
 */

class DeleteNewsByCountryUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(country: Country) {
        repository.deleteNewsByCountry(country)
    }
}