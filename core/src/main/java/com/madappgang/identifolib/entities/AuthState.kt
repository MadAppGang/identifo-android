package com.madappgang.identifolib.entities


/*
 * Created by Eugene Prytula on 2/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

sealed class AuthState {
    data class Authentificated(
        val identifoUser: IdentifoUser?,
        val accessToken : String?
    ) : AuthState()

    object Deauthentificated : AuthState()
}