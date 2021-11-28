package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.domain.repository.NewsRepository
import java.io.IOException
import javax.inject.Inject

/**
 * UseCase for clearing all news by country in database
 */

class DeleteNewsByCountryUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(country: Country) {
        return try {
            repository.deleteNewsByCountry(country)
        } catch (e: IOException) {
            throw Error("Error on all news deleting")
        }
    }
}