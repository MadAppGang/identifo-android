package com.prytula.identifolibui.login.username

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

class UsernameLoginViewModel : ViewModel() {
    private val _loginUIState = MutableStateFlow<UsernameLoginUIStates>(UsernameLoginUIStates.IDLE)
    val loginUIStates: StateFlow<UsernameLoginUIStates> = _loginUIState.asStateFlow()

    fun performLogin(username: String, password: String) {
        viewModelScope.launch {
            _loginUIState.emit(UsernameLoginUIStates.Loading)
            IdentifoAuthentication.loginWithUsernameAndPassword(username, password)
                .onSuccess { loginResponse ->
                    _loginUIState.emit(UsernameLoginUIStates.LoginSuccessful(loginResponse))
                }.onError { errorResponse ->
                    _loginUIState.emit(UsernameLoginUIStates.LoginFailure(errorResponse))
                }
        }
    }
}