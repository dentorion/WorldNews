package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.UseCaseResult
import com.entin.worldnews.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Get list of favourite news from database UseCase
 * Business Logic placed here
 */

class GetFavouriteNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Flow<UseCaseResult> = flow {
        repository.getFavouriteNews().collect {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)
}