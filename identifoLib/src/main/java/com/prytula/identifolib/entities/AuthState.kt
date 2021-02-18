package com.prytula.identifolib.entities


/*
 * Created by Eugene Prytula on 2/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

sealed class AuthState {
    data class Authentificated(
        val anonymousState: Boolean = false
    ) : AuthState()

    object Deauthentificated : AuthState()
}