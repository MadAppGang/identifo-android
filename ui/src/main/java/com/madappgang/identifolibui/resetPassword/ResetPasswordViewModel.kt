package com.madappgang.identifolibui.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madappgang.IdentifoAuthentication
import com.madappgang.identifolib.entities.ErrorResponse
import com.madappgang.identifolib.extensions.onError
import com.madappgang.identifolib.extensions.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 3/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class ResetPasswordViewModel : ViewModel() {
    private val _passwordHasBeenReset = MutableSharedFlow<String>()
    val passwordHasBeenReset: SharedFlow<String> = _passwordHasBeenReset

    private val _receiveError = MutableSharedFlow<ErrorResponse>()
    val receiveError: SharedFlow<ErrorResponse> = _receiveError

    fun resetPassword(email: String) {
        viewModelScope.launch {
            IdentifoAuthentication.resetPassword(email).onSuccess { resetPasswordResponse ->
                _passwordHasBeenReset.emit(email)
            }.onError { errorResponse ->
                _receiveError.emit(errorResponse)
            }
        }
    }
}