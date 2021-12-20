package com.entin.worldnews.presentation.ui.search

import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.presentation.ui.country.components.ExceptionMessage
import com.entin.worldnews.presentation.util.ViewState

/**
 * Inside Ui State of Search Fragment
 */
data class ViewStateSearch(
    val news: List<Article> = listOf(),
    val initial: Boolean = true,
    val error: Boolean = false,
    val empty: Boolean = false,
    override val exception: Throwable? = null,
    override val exceptionMessage: ExceptionMessage? = null,
): ViewState