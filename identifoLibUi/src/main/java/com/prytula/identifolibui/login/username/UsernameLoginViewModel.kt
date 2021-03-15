package com.prytula.identifolibui.login.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.entities.ErrorResponse
import com.prytula.identifolib.entities.logging.LoginResponse
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class UsernameLoginViewModel : ViewModel() {
    private val _signInSuccessful = MutableSharedFlow<LoginResponse>()
    val signInSuccessful: SharedFlow<LoginResponse> = _signInSuccessful

    private val _receiveError = MutableSharedFlow<ErrorResponse>()
    val receiveError: SharedFlow<ErrorResponse> = _receiveError

    fun performLogin(username: String, password: String) {
        viewModelScope.launch {
            IdentifoAuth.loginWithUsernameAndPassword(username, password)
                .onSuccess { loginResponse ->
                    _signInSuccessful.emit(loginResponse)
                }.onError { errorResponse ->
                    _receiveError.emit(errorResponse)
                }
        }
    }
}