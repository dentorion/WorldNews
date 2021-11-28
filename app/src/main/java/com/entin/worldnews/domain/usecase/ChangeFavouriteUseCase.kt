package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.repository.NewsRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UseCase for changing boolean status of Article in favourite
 */

class ChangeFavouriteUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(url: String) {
        try {
            repository.changeFavouriteArticle(url = url)
        } catch (e: IOException) {
            throw Error(e.message.toString())
        }
    }
}