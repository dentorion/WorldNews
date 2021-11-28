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
 * Get list of favourite news from database
 */

class GetFavouriteNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Flow<UseCaseResult> = flow {
        try {
            withTimeout(FIVE_SECONDS) {
                repository.getFavouriteNews().collect {
                    emit(checkEmptyResult(it))
                }
            }
        } catch (e: IOException) {
            emit(UseCaseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}