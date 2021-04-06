package com.prytula.identifolibui.login.phoneNumber.oneTimePassword

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prytula.IdentifoAuthentication
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import kotlinx.coroutines.flow.*
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

    private val _oneTimePasswordUIState = MutableStateFlow<OneTimePasswordUIStates>(OneTimePasswordUIStates.IDLE)
    val oneTimePasswordUIState: StateFlow<OneTimePasswordUIStates> = _oneTimePasswordUIState.asStateFlow()

    private val _sendCodeUIState = MutableStateFlow<OneTimePasswordTimerStates>(OneTimePasswordTimerStates.PossibleToSendCode)
    val sendCodeUIState : StateFlow<OneTimePasswordTimerStates> = _sendCodeUIState.asStateFlow()

    private val coutDownTimer = object : CountDownTimer(TOTAL_WAITING_TiME_MILLIS, STEP_TIME_MILLIS) {
        override fun onTick(millisUntilFinished: Long) {
            _sendCodeUIState.value = OneTimePasswordTimerStates.TimerClick(millisUntilFinished)
        }

        override fun onFinish() {
            _sendCodeUIState.value = OneTimePasswordTimerStates.PossibleToSendCode
        }
    }

    fun requestOtpCode(phoneNumber: String) {
        viewModelScope.launch {
            IdentifoAuthentication.requestPhoneCode(phoneNumber).onSuccess { requestPhoneResponse ->
                coutDownTimer.start()
            }.onError { errorResponse ->
                _sendCodeUIState.emit(OneTimePasswordTimerStates.PossibleToSendCode)
            }
        }
    }

    fun loginViaPhoneNumber(phoneNumber: String, otp: String) {
        viewModelScope.launch {
            _oneTimePasswordUIState.emit(OneTimePasswordUIStates.Loading)
            IdentifoAuthentication.phoneLogin(phoneNumber, otp).onSuccess { phoneLoginResponse ->
                _oneTimePasswordUIState.emit(OneTimePasswordUIStates.LoginSuccessful(phoneLoginResponse))
            }.onError { errorResponse ->
                _oneTimePasswordUIState.emit(OneTimePasswordUIStates.LoginFailure(errorResponse))
            }
        }
    }
}