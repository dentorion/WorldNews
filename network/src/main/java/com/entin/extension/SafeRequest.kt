package com.entin.extension

import retrofit2.HttpException

suspend fun <T: Any> handleRequest(requestFunc: suspend () -> T): Result<T> {
    return try {
        Result.success(requestFunc.invoke())
    } catch (he: HttpException) {
        Result.failure(he)
    }
}