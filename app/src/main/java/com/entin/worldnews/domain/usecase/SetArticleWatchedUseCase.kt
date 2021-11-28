package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.repository.NewsRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UseCase for setting Article as watched in database
 * Eye label
 */

@Singleton
class SetArticleWatchedUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(url: String) {
        try {
            repository.setArticleShown(url)
        } catch (e: IOException) {
            throw Error("Error on setting news as watched")
        }
    }
}