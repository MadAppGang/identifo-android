package com.madappgang.identifolib.extensions


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

sealed class Result<out T, out E> {
    data class Success<T>(val value: T) : Result<T, Nothing>()
    data class Error<E>(val error: E) : Result<Nothing, E>()
}

fun <T, E> Result<T, E>.isSuccessful() = this is Result.Success
fun <T, E> Result<T, E>.isUnsuccessful() = this is Result.Error


suspend inline fun <D : Throwable, T, E : D> Result<T, E>.onSuccess(crossinline block: suspend (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Success -> {
            block(this.value)
            this
        }
        is Result.Error -> Result.Error(this.error)
    }
}

class FlowCancelException : Throwable()


suspend inline fun <D : Throwable, T, E : D> Result<T, E>.onError(crossinline block: suspend (E) -> Unit): Result<T, D> {
    return when (this) {
        is Result.Success -> this
        is Result.Error -> {
            val error = this.error
            if (error !is FlowCancelException) {
                block(error)
                return Result.Error(FlowCancelException() as D)
            }
            this
        }
    }
}