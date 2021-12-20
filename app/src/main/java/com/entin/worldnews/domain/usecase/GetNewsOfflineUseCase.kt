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
 * Get OFF-line News by Country UseCase
 * Business Logic placed here
 */

class GetNewsOfflineUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(country: Country): Flow<UseCaseResult> = flow {
        repository.getOfflineNews(country).collect { result ->
            emit(result)
        }
    }.flowOn(Dispatchers.IO)
}