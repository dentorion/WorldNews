package com.entin.worldnews.presentation.util

import com.entin.worldnews.presentation.ui.country.components.ExceptionMessage

/**
 * Interface for all fragment's viewState.
 * Inside data of ViewModelResult.
 */
interface ViewState {
    val exception: Throwable?
    val exceptionMessage: ExceptionMessage?
}