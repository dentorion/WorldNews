package com.entin.worldnews.presentation.ui.country.components

import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.presentation.util.ViewState

/**
 * Inside Ui State of Country Fragment
 */
data class CountryViewState(
    val news: List<Article> = listOf(),
    val isForced: Boolean = false,
    val isEmpty: Boolean = false,
    override val exception: Throwable? = null,
    override val exceptionMessage: ExceptionMessage? = null,
): ViewState

sealed class ExceptionMessage {
    object NoInternet: ExceptionMessage()
}