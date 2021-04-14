package com.madappgang.identifolibui.login.username

import com.madappgang.identifolib.entities.ErrorResponse
import com.madappgang.identifolib.entities.logging.LoginResponse


/*
 * Created by Eugene Prytula on 4/5/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

sealed class UsernameLoginUIStates {
    class LoginSuccessful(val loginResponse: LoginResponse) : UsernameLoginUIStates()
    class LoginFailure(val error: ErrorResponse) : UsernameLoginUIStates()
    object Loading : UsernameLoginUIStates()
    object IDLE : UsernameLoginUIStates()
}