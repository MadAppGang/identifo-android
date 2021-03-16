package com.prytula.identifolibui.login.phoneNumber.oneTimePassword

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.entities.ErrorResponse
import com.prytula.identifolib.entities.phoneLogin.PhoneLoginResponse
import com.prytula.identifolib.entities.requestCode.RequestPhoneCodeResponse
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class OneTimePasswordViewModel : ViewModel() {

    companion object {
        private const val TOTAL_WAITING_TiME_MILLIS = 60000L
        private const val STEP_TIME_MILLIS = 1000L
    }

    private val _finishSigningIn = MutableSharedFlow<PhoneLoginResponse>()
    val finishSigningIn: SharedFlow<PhoneLoginResponse> = _finishSigningIn

    private val _receiveError = MutableSharedFlow<ErrorResponse>()
    val receiveError: SharedFlow<ErrorResponse> = _receiveError

    private val _isImpossibleToSendTheCode = MutableStateFlow<Boolean>(false)
    val isImpossibleToSendTheCode: StateFlow<Boolean> = _isImpossibleToSendTheCode

    private val _timerClickValue = MutableStateFlow<Long>(0L)
    val timerClickValue: StateFlow<Long> = _timerClickValue

    private val coutDownTimer = object : CountDownTimer(TOTAL_WAITING_TiME_MILLIS, STEP_TIME_MILLIS) {
        override fun onTick(millisUntilFinished: Long) {
            _timerClickValue.value = millisUntilFinished
        }

        override fun onFinish() {
            _isImpossibleToSendTheCode.value = true
        }
    }

    fun requestOtpCode(phoneNumber: String) {
        viewModelScope.launch {
            IdentifoAuth.requestPhoneCode(phoneNumber).onSuccess { requestPhoneResponse ->
                _isImpossibleToSendTheCode.value = false
                coutDownTimer.start()
            }.onError { errorResponse ->
                _receiveError.emit(errorResponse)
            }
        }
    }

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