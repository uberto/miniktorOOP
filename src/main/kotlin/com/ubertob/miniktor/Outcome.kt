package com.ubertob.miniktor


sealed interface Outcome<out T>{
    fun <B> transform(fn: (T) -> B): Outcome<B> =
        when (this) {
            is Failure -> this
            is Success -> Success(fn(value))
        }

    fun <B> bind(fn: (T) -> Outcome<B>): Outcome<B> =
        when (this) {
            is Failure -> this
            is Success -> fn(value)
        }


}

data class Success<T>(val value: T): Outcome<T>
data class Failure(val errorMsg: String): Outcome<Nothing>

fun <T> Outcome<T>.recover(onError: (String) -> T): T =
    when(this){
        is Failure -> onError(errorMsg)
        is Success -> value
    }