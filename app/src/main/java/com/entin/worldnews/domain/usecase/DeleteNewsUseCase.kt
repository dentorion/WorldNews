package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.domain.repository.NewsRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend fun execute(country: Country) {
        return try {
            repository.deleteNewsByCountry(country)
        } catch (e: IOException) {
            throw Error("Error on all news deleting")
        }
    }
}