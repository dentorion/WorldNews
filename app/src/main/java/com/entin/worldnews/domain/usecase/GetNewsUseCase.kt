package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    sealed class UseCaseResult {
        data class Success(val data: List<Article>) : UseCaseResult()
        data class Error(val e: Throwable) : UseCaseResult()
        object Empty : UseCaseResult()
    }

    suspend fun execute(country: Country, forced: Boolean): UseCaseResult {
        return try {
            withTimeout(TIME_OUT) {
                val response = if (forced) {
                    repository.forcedNewsDownload(country).first()
                } else {
                    repository.getNews(country).first()
                }

                if (response.isNotEmpty()) {
                    UseCaseResult.Success(response)
                } else {
                    UseCaseResult.Empty
                }
            }
        } catch (e: IOException) {
            UseCaseResult.Error(e)
        }
    }
}

// Five seconds
private const val TIME_OUT = 5000L