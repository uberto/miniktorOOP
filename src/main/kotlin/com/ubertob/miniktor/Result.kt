package com.ubertob.miniktor

import io.ktor.http.*

sealed class Result<T>
data class Failure(val error: Error): Result<Nothing>()
data class Success<T>(val value: T): Result<T>()


sealed interface Error{
    val msg: String
}
data class RequestError(override val msg: String, val request: HttpMessage): Error
data class DbError(override val msg: String, val exception: Exception? = null): Error
