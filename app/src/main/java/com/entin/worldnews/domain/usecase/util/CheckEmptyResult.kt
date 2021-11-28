package com.entin.worldnews.domain.usecase.util

import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.domain.model.UseCaseResult

fun checkEmptyResult(listOfArticles: List<Article>): UseCaseResult {
    return if (listOfArticles.isNotEmpty()) {
        UseCaseResult.Success(listOfArticles)
    } else {
        UseCaseResult.Empty
    }
}