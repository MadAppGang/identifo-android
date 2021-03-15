package com.prytula.identifolibui.login.phoneNumber.phoneNumberLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.entities.ErrorResponse
import com.prytula.identifolib.entities.requestCode.RequestPhoneCodeResponse
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class PhoneNumberViewModel : ViewModel() {

    private val _codeHasBeenReceived = MutableSharedFlow<RequestPhoneCodeResponse>()
    val codeHasBeenReceived: SharedFlow<RequestPhoneCodeResponse> = _codeHasBeenReceived

    private val _receiveError = MutableSharedFlow<ErrorResponse>()
    val receiveError: SharedFlow<ErrorResponse> = _receiveError

    fun requestOtpCode(phoneNumber: String) {
        viewModelScope.launch {
            IdentifoAuth.requestPhoneCode(phoneNumber).onSuccess { requestPhoneResponse ->
                _codeHasBeenReceived.emit(requestPhoneResponse)
            }.onError { errorResponse ->
                _receiveError.emit(errorResponse)
            }
        }
    }
}