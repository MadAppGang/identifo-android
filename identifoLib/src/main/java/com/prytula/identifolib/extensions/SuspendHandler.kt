package com.prytula.identifolib.extensions

import com.prytula.identifolib.entities.CodedThrowable
import com.prytula.identifolib.entities.ErrorCodes
import retrofit2.HttpException


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

suspend fun <T> suspendApiCall(requestFunc: suspend () -> T): Result<T, CodedThrowable> {
    return try {
        val request = requestFunc.invoke()
        Result.Success(request)
    } catch (e: Exception) {
        val error = when (e) {
            is HttpException -> CodedThrowable(e.localizedMessage, ErrorCodes.valueCodeOf(e.code()))
            else -> CodedThrowable(e.localizedMessage, ErrorCodes.UNDEFINED_ERROR)
        }
        return Result.Error(error)
    }
}