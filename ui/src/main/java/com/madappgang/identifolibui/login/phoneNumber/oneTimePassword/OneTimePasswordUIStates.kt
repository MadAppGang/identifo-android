package com.madappgang.identifolibui.login.phoneNumber.oneTimePassword

import com.madappgang.identifolib.entities.ErrorResponse
import com.madappgang.identifolib.entities.phoneLogin.PhoneLoginResponse


/*
 * Created by Eugene Prytula on 4/5/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

sealed class OneTimePasswordUIStates {
    class LoginSuccessful(val phoneLoginResponse: PhoneLoginResponse) : OneTimePasswordUIStates()
    class LoginFailure(val error: ErrorResponse) : OneTimePasswordUIStates()
    object Loading : OneTimePasswordUIStates()
    object IDLE : OneTimePasswordUIStates()
}