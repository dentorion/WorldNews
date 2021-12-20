package com.entin.worldnews.presentation.ui.favourite

import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.presentation.ui.country.components.ExceptionMessage
import com.entin.worldnews.presentation.util.ViewState

/**
 * Inside Ui State of Favourite Fragment
 */
data class ViewStateFavourites(
    val news: List<Article> = listOf(),
    val empty: Boolean = false,
    val deleted: Boolean = false,
    override val exception: Throwable? = null,
    override val exceptionMessage: ExceptionMessage? = null,
): ViewState