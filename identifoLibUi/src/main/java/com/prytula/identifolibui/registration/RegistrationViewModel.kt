package com.prytula.identifolibui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prytula.IdentifoAuthentication
import com.prytula.identifolib.entities.ErrorResponse
import com.prytula.identifolib.entities.register.RegisterResponse
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class RegistrationViewModel : ViewModel() {

    private val _registrationSuccessful = MutableSharedFlow<RegisterResponse>()
    val registrationSuccessful: SharedFlow<RegisterResponse> = _registrationSuccessful

    private val _receiveError = MutableSharedFlow<ErrorResponse>()
    val receiveError: SharedFlow<ErrorResponse> = _receiveError

    fun registerWithUsernameAndPassword(username: String, password: String) {
        viewModelScope.launch {
            IdentifoAuthentication.registerWithUsernameAndPassword(username, password, false)
                .onSuccess { registerResponse ->
                    _registrationSuccessful.emit(registerResponse)
                }.onError { errorResponse ->
                    _receiveError.emit(errorResponse)
                }
        }
    }
}