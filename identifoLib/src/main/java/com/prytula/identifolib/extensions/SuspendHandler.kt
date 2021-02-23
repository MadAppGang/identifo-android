package com.prytula.identifolib.extensions

import com.google.gson.Gson
import com.prytula.identifolib.entities.Error
import com.prytula.identifolib.entities.ErrorResponse
import retrofit2.HttpException
import java.lang.Exception


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

suspend fun <T> suspendApiCall(requestFunc: suspend () -> T): Result<T, ErrorResponse> {
    return try {
        val request = requestFunc.invoke()
        Result.Success(request)
    } catch (e: Throwable) {
        val error = when (e) {
            is HttpException -> parseErrorBody(e)
            else -> ErrorResponse(
                Error(
                    message = e.localizedMessage.toString(),
                    detailedMessage = e.message.toString()
                )
            )
        }
        return Result.Error(error)
    }
}

private fun parseErrorBody(httpException: HttpException): ErrorResponse {
    return try {
        Gson().fromJson(
            httpException.response()?.errorBody()?.string(),
            ErrorResponse::class.java
        )
    } catch (e: Exception) {
        ErrorResponse(
            Error(
                message = e.localizedMessage.toString(),
                detailedMessage = e.message.toString()
            )
        )
    }
}