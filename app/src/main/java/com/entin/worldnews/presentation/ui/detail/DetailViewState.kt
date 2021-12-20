package com.entin.worldnews.presentation.ui.detail

import com.entin.worldnews.presentation.ui.country.components.ExceptionMessage
import com.entin.worldnews.presentation.util.ViewState

/**
 * Inside Ui State of Detail Fragment
 */
data class DetailViewState(
    val title: String,
    val author: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val publishedTime: String,
    override val exception: Throwable? = null,
    override val exceptionMessage: ExceptionMessage? = null,
): ViewState