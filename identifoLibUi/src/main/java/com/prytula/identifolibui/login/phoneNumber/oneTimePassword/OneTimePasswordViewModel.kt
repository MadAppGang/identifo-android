package com.prytula.identifolibui.login.phoneNumber.oneTimePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.entities.ErrorResponse
import com.prytula.identifolib.entities.phoneLogin.PhoneLoginResponse
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class OneTimePasswordViewModel : ViewModel() {
    private val _finishSigningIn = MutableSharedFlow<PhoneLoginResponse>()
    val finishSigningIn: SharedFlow<PhoneLoginResponse> = _finishSigningIn

    private val _receiveError = MutableSharedFlow<ErrorResponse>()
    val receiveError: SharedFlow<ErrorResponse> = _receiveError

    fun loginViaPhoneNumber(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            IdentifoAuth.phoneLogin(phoneNumber, otp).onSuccess { phoneLoginResponse ->
                _finishSigningIn.emit(phoneLoginResponse)
            }.onError { errorResponse ->
                _receiveError.emit(errorResponse)
            }
        }
    }
}