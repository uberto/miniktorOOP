package com.ubertob.miniktor

sealed interface Outcome<out T> {

    fun <U> transform(fn: (T) -> U): Outcome<U> =
        when (this) {
            is Failure -> this
            is Success -> Success(fn(value))
        }

    fun <U> bind(fn: (T) -> Outcome<U>): Outcome<U> =
        when (this) {
            is Failure -> this
            is Success -> fn(value)
        }
}

data class Failure(val msg: String) : Outcome<Nothing>
data class Success<T>(val value: T) : Outcome<T>


fun <T> Outcome<T>.recover(fn: (String) -> T): T =
    when (this) {
        is Failure -> fn(this.msg)
        is Success -> this.value
    }
