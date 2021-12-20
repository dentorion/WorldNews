package com.entin.extension

/**
 * Extension function to help make safe internet request
 * Returns Kotlin Result success with result or failure with exception
 */

suspend fun <T: Any> handleRequest(requestFunc: suspend () -> T): Result<T> {
    return try {
        Result.success(requestFunc.invoke())
    } catch (e: Exception) {
        Result.failure(e)
    }
}