package com.madappgang.identifolibui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madappgang.IdentifoAuthentication
import com.madappgang.identifolib.extensions.onError
import com.madappgang.identifolib.extensions.onSuccess
import com.madappgang.identifolibui.registration.RegistrationUIStates.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class RegistrationViewModel : ViewModel() {

    private val _registrationUIState = MutableStateFlow<RegistrationUIStates>(IDLE)
    val registrationUIState: StateFlow<RegistrationUIStates> = _registrationUIState.asStateFlow()

    fun registerWithUsernameAndPassword(username: String, password: String) {
        viewModelScope.launch {
            _registrationUIState.emit(Loading)
            IdentifoAuthentication.registerWithUsernameAndPassword(username, password, false)
                .onSuccess { registerResponse ->
                    _registrationUIState.emit(RegistrationSuccessful(registerResponse))
                }.onError { errorResponse ->
                    _registrationUIState.emit(RegistrationFailure(errorResponse))
                }
        }
    }
}