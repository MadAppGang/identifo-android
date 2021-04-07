package com.madappgang.identifolib.storages

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.madappgang.identifolib.entities.IdentifoUser


/*
 * Created by Eugene Prytula on 2/25/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

interface IUserStorage {
    fun setUser(identifoUser: IdentifoUser)
    fun getUser(): IdentifoUser
    fun clearAll()
}

class UserStorage(context: Context) : IUserStorage {

    companion object {
        private const val IDENTIFO_USER_STORAGE = "identifo_user_storage"
        private const val USER_ID_KEY = "user_id_key"
        private const val USER_NAME_KEY = "user_name_key"
        private const val USER_IS_ANONYMOUS_KEY = "user_is_anonymous_key"
    }

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        IDENTIFO_USER_STORAGE,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun setUser(identifoUser: IdentifoUser) {
        with(sharedPreferences.edit()) {
            putString(USER_ID_KEY, identifoUser.id)
            putString(USER_NAME_KEY, identifoUser.username)
            putBoolean(USER_IS_ANONYMOUS_KEY, identifoUser.isAnonymous)
            apply()
        }
    }

    override fun getUser(): IdentifoUser {
        val id = sharedPreferences.getString(USER_ID_KEY, "") ?: ""
        val userName = sharedPreferences.getString(USER_NAME_KEY, "") ?: ""
        val isAnonymous = sharedPreferences.getBoolean(USER_IS_ANONYMOUS_KEY, false)
        return IdentifoUser(id, userName, isAnonymous)
    }


    override fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}