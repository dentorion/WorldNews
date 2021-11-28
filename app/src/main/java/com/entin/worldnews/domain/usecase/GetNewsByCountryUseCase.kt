package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.Country
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
 * Get News from api server
 * Can ask for news in Repository in normal way - updates not often than 2 Hours
 * or in forced way - without waiting any time.
 *
 * [FIVE_SECONDS] - time out for network response
 */

class GetNewsByCountryUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(country: Country, forced: Boolean): Flow<UseCaseResult> = flow {
        try {
            withTimeout(FIVE_SECONDS) {
                if (forced) {
                    repository.forcedNewsDownload(country).collect { listOfArticles ->
                        emit(checkEmptyResult(listOfArticles))
                    }
                } else {
                    repository.getNews(country).collect { listOfArticles ->
                        emit(checkEmptyResult(listOfArticles))
                    }
                }
            }
        } catch (e: IOException) {
            emit(UseCaseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}