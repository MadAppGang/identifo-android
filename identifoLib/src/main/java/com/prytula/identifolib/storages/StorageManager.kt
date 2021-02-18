package com.prytula.identifolib.storages

import android.content.Context
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.google.gson.GsonBuilder
import com.prytula.identifolib.*
import com.prytula.identifolib.entities.Token
import com.prytula.identifolib.entities.Tokens


/*
 * Created by Eugene Prytula on 2/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

interface IStorageManager {
    fun tokenManager(): ITokenDataStorage
}

class StorageManager(
    private val context: Context
) : IStorageManager {
    init {
        Kotpref.apply {
            init(context)
            gson = GsonBuilder()
                .registerTypeAdapter(Tokens::class.java, TokensTypeAdapter())
                .registerTypeAdapter(Token.Access::class.java, AccessTokenTypeAdapter())
                .create()
        }
    }

    override fun tokenManager(): ITokenDataStorage = TokenDataStorage(context)
}