package com.ubertob.miniktor

import io.ktor.http.*

sealed class Result<out T> {
    fun <U> transform(f: (T) -> U): Result<U> =
        when(this){
            is Success -> Success(f(value))
            is Failure -> this
        }
}
data class Failure(val error: Error) : Result<Nothing>()
data class Success<T>(val value: T) : Result<T>()


sealed interface Error {
    val msg: String
}

data class RequestError(override val msg: String, val request: HttpMessage) : Error
data class DbError(override val msg: String, val exception: Exception? = null) : Error
data class ResponseError(override val msg: String, val statusCode: HttpStatusCode, val cause: Error? = null) : Error


fun <T> T.asSuccess(): Result<T> = Success(this)
fun <E : Error> E.asFailure(): Result<Nothing> = Failure(this)


fun <T> Result<T>.orThrow(): T =
    when(this){
        is Failure -> throw ResultException(error)
        is Success -> value
    }

data class ResultException(val error: Error) : Exception()

fun <T: Any> T?.failIfNull(error: Error): Result<T> =
