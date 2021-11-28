package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.UseCaseResult
import com.entin.worldnews.domain.repository.NewsRepository
import com.entin.worldnews.domain.usecase.util.FIVE_SECONDS
import com.entin.worldnews.domain.usecase.util.checkEmptyResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import java.io.IOException
import javax.inject.Inject

/**
 * UseCase for searching news by query
 */

class SearchNewsByQueryUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(search: String): Flow<UseCaseResult> = flow {
        try {
            withTimeout(FIVE_SECONDS) {
                repository.getSearchNews(search).collect {
                    emit(checkEmptyResult(it))
                }
            }
        } catch (e: IOException) {
            emit(UseCaseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}