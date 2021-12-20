package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.domain.model.UseCaseResult
import com.entin.worldnews.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Get News by Country UseCase
 * Business Logic placed here
 *
 * Get News from api server
 * User can download actual news not often than once in 2 Hours in normal way
 * or in forced way - without waiting any time.
 */

class GetNewsByCountryUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(country: Country, forced: Boolean): Flow<UseCaseResult> = flow {
        if (forced) {
            repository.forcedNewsDownload(country).collect { result ->
                emit(result)
            }
        } else {
            repository.getNews(country).collect { result ->
                emit(result)
            }
        }
    }.flowOn(Dispatchers.IO)
}