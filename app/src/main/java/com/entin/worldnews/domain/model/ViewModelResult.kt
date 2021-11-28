package com.entin.worldnews.domain.model

import java.lang.IllegalArgumentException

typealias Mapper<Input, Output> = (Input) -> Output

sealed class ViewModelResult<T> {

    // Future update: mapping article models from source into room and domain models
    // inside WorldNewsResult wrapper
    fun <R> map(mapper: Mapper<T, R>? = null): ViewModelResult<R> = when (this) {
        is ErrorResult -> ErrorResult(this.exception)
        is PendingResult -> PendingResult()
        is SuccessResult -> {
            if(mapper == null) throw IllegalArgumentException("Mapper is null!")
            SuccessResult(mapper(this.data))
        }
    }
}

class PendingResult<T> : ViewModelResult<T>()

class SuccessResult<T>(val data: T) : ViewModelResult<T>()

class ErrorResult<T>(val exception: String) : ViewModelResult<T>()

// Returns only Success result inside data or null
fun <T> ViewModelResult<T>?.takeSuccessOnly(): T? {
    return if (this is SuccessResult) {
        this.data
    } else {
        null
    }
}