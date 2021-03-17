package com.prytula.identifolib.storages

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
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

class TokenDataStorage(context: Context) : ITokenDataStorage {

    companion object {
        private const val TOKENS_STORAGE = "tokens_storage"
        private const val ACCESS_TOKEN_KEY = "access_token_key"
        private const val REFRESH_TOKEN_KEY = "refresh_token_key"
    }

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences : SharedPreferences = EncryptedSharedPreferences.create(
        TOKENS_STORAGE,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    @Synchronized
    override fun getTokens(): Tokens {
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, "")
        val refreshToken = sharedPreferences.getString(REFRESH_TOKEN_KEY, "")

        val accessTokenEntity: Token.Access? =
            accessToken?.takeIf { it.isNotBlank() }?.let { Token.Access(it) }
        val refreshTokenEntity: Token.Refresh? =
            refreshToken?.takeIf { it.isNotBlank() }?.let { Token.Refresh(it) }

        return Tokens(accessTokenEntity, refreshTokenEntity)
    }

    @Synchronized
    override fun setTokens(tokens: Tokens) {
        val accessToken = tokens.access
        val refreshToken = tokens.refresh

        with(sharedPreferences.edit()) {
            putString(ACCESS_TOKEN_KEY, accessToken?.jwtEncoded)
            putString(REFRESH_TOKEN_KEY, refreshToken?.jwtEncoded)
            apply()
        }
    }

    override fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}