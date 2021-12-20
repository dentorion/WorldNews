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
 * Searching News by Query UseCase
 * Business Logic placed here
 */

class SearchNewsByQueryUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(search: String): Flow<UseCaseResult> = flow {
        repository.getSearchNews(search).collect {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)
}