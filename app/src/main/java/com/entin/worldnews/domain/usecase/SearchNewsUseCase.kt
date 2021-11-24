package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.domain.repository.NewsRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    sealed class UseCaseResult {
        data class Success(val data: List<Article>) : UseCaseResult()
        data class Error(val e: Throwable) : UseCaseResult()
        object Empty : UseCaseResult()
    }

    suspend fun saveSearchedAndOpenedArticle(article: Article) =
        repository.saveSearchedAndOpenedArticle(article)

    suspend fun execute(search: String): UseCaseResult {
        return try {
            val response: List<Article> = repository.getSearchNews(search)

            if (response.isNotEmpty()) {
                UseCaseResult.Success(response)
            } else {
                UseCaseResult.Empty
            }
        } catch (e: IOException) {
            UseCaseResult.Error(e)
        }
    }
}