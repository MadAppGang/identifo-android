package com.madappgang.identifolibui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madappgang.IdentifoAuthentication
import com.madappgang.identifolib.entities.ErrorResponse
import com.madappgang.identifolib.entities.federatedLogin.FederatedLoginResponse
import com.madappgang.identifolib.extensions.onError
import com.madappgang.identifolib.extensions.onSuccess
import com.madappgang.identifolib.entities.FederatedProviders
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class CommonViewModel : ViewModel() {
    private val _finishSigningIn = MutableSharedFlow<FederatedLoginResponse>()
    val finishSigningIn: SharedFlow<FederatedLoginResponse> = _finishSigningIn

    private val _receiveError = MutableSharedFlow<ErrorResponse>()
    val receiveError: SharedFlow<ErrorResponse> = _receiveError

    fun sendFederatedToken(federatedProvider: FederatedProviders, token: String) {
        viewModelScope.launch {
            IdentifoAuthentication.federatedLogin(federatedProvider.title, token)
                .onSuccess { federatedLoginResponse ->
                    _finishSigningIn.emit(federatedLoginResponse)
                }.onError { errorResponse ->
                    _receiveError.emit(errorResponse)
                }
        }
    }
}