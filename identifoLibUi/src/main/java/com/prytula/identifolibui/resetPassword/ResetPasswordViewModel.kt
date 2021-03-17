package com.prytula.identifolibui.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.entities.ErrorResponse
import com.prytula.identifolib.entities.reserPassword.ResetPasswordResponse
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 3/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class ResetPasswordViewModel : ViewModel() {
    private val _passwordHasBeenReset = MutableSharedFlow<ResetPasswordResponse>()
    val passwordHasBeenReset: SharedFlow<ResetPasswordResponse> = _passwordHasBeenReset

    private val _receiveError = MutableSharedFlow<ErrorResponse>()
    val receiveError: SharedFlow<ErrorResponse> = _receiveError

    fun resetPassword(email: String) {
        viewModelScope.launch {
            IdentifoAuth.resetPassword(email).onSuccess { resetPasswordResponse ->
                _passwordHasBeenReset.emit(resetPasswordResponse)
            }.onError { errorResponse ->
                _receiveError.emit(errorResponse)
            }
        }
    }
}