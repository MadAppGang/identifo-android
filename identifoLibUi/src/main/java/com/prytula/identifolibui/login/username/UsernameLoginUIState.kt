package com.prytula.identifolibui.login.username

import com.prytula.identifolib.entities.ErrorResponse
import com.prytula.identifolib.entities.logging.LoginResponse


/*
 * Created by Eugene Prytula on 4/5/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

sealed class UsernameLoginUIState {
    class LoginSuccessful(val loginResponse: LoginResponse) : UsernameLoginUIState()
    class LoginFailure(val error: ErrorResponse) : UsernameLoginUIState()
    object Loading : UsernameLoginUIState()
    object IDLE : UsernameLoginUIState()
}