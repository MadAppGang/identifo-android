package com.prytula.identifolib.storages

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import com.prytula.identifolib.entities.Token
import com.prytula.identifolib.entities.Tokens


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

interface ITokenDataStorage {
    fun getTokens(): Tokens
    fun setTokens(tokens: Tokens)
    fun clearAll()
}

class TokenDataStorage(context: Context) : KotprefModel(context), ITokenDataStorage {

    @Synchronized
    override fun getTokens(): Tokens {
        return token
    }

    @Synchronized
    override fun setTokens(tokens: Tokens) {
        token = tokens
    }

    override fun clearAll() = clear()

    private var accessToken: String by stringPref("")
    private var refreshToken: String by stringPref("")

    private var token: Tokens
        get() {
            val access = accessToken.takeIf { it.isNotBlank() }?.let { Token.Access(it) }
            val refresh = refreshToken.takeIf { it.isNotBlank() }?.let { Token.Refresh(it) }
            return Tokens(access, refresh)
        }
        set(value) {
            accessToken = value.access?.jwtEncoded ?: ""
            refreshToken = value.refresh?.jwtEncoded ?: ""
        }
}