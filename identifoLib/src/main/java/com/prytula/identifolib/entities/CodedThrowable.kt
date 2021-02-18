package com.prytula.identifolib.entities

import java.net.HttpURLConnection


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class CodedThrowable(message: String?, val code: ErrorCodes) : Throwable(message)

enum class ErrorCodes {
    UNDEFINED_ERROR,
    NO_INTERNET_ERROR,
    HTTP_NOT_FOUND_ERROR,
    UNAUTHORIZED;

    companion object {
        fun valueCodeOf(code: Int): ErrorCodes {
            return when (code) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> UNAUTHORIZED
                HttpURLConnection.HTTP_NOT_FOUND -> HTTP_NOT_FOUND_ERROR
                else -> UNDEFINED_ERROR
            }
        }
    }
}