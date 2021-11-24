package com.entin.worldnews.domain.model

import java.lang.IllegalArgumentException

typealias Mapper<Input, Output> = (Input) -> Output

sealed class WorldNewsResult<T> {

    fun <R> map(mapper: Mapper<T, R>? = null): WorldNewsResult<R> = when (this) {
        is ErrorResult -> ErrorResult(this.exception)
        is PendingResult -> PendingResult()
        is SuccessResult -> {
            if(mapper == null) throw IllegalArgumentException("Mapper is null!")
            SuccessResult(mapper(this.data))
        }
    }
}

class PendingResult<T> : WorldNewsResult<T>()

class SuccessResult<T>(val data: T) : WorldNewsResult<T>()

class ErrorResult<T>(val exception: String) : WorldNewsResult<T>()

fun <T> WorldNewsResult<T>?.takeSuccessOnly(): T? {
    return if (this is SuccessResult) {
        this.data
    } else {
        null
    }
}