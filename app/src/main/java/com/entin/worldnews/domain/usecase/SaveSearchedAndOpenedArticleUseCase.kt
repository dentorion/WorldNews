package com.entin.worldnews.domain.usecase

import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UseCase for saving Article in database after it was opened in search result
 */

@Singleton
class SaveSearchedAndOpenedArticleUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(article: Article) =
        repository.saveSearchedAndOpenedArticle(article)
}