package com.prytula.identifolibui.registration

import com.prytula.identifolib.entities.ErrorResponse
import com.prytula.identifolib.entities.register.RegisterResponse


/*
 * Created by Eugene Prytula on 4/2/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

sealed class RegistrationUIStates {
    class RegistrationSuccessful(registerResponse: RegisterResponse) : RegistrationUIStates()
    class RegistrationFailure(val error: ErrorResponse) : RegistrationUIStates()
    object Loading : RegistrationUIStates()
    object IDLE : RegistrationUIStates()
}