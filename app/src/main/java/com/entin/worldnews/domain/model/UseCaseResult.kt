package com.entin.worldnews.domain.model

/**
 * Special sealed class to wrap result from UseCase to ViewModel
 */

sealed class UseCaseResult {
    data class Success(val data: List<Article>) : UseCaseResult()
    data class Error(val e: Throwable) : UseCaseResult()
    object Empty : UseCaseResult()
}