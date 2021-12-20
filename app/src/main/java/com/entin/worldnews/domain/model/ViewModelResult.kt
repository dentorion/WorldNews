package com.entin.worldnews.domain.model

sealed class ViewModelResult<T>

/**
 * LOADING state
 */
class PendingResult<T> : ViewModelResult<T>()

/**
 * SUCCESS state
 */
class SuccessResult<T>(val data: T) : ViewModelResult<T>()

/**
 * ERROR state
 */
class ErrorResult<T>(val data: T) : ViewModelResult<T>()

/**
 * Future use
 * Returns only Success result inside data or null
 */
fun <T> ViewModelResult<T>?.takeSuccessOnly(): T? {
    return if (this is SuccessResult) {
        this.data
    } else {
        null
    }
}