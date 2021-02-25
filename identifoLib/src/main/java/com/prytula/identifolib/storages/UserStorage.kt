package com.prytula.identifolib.storages

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.gsonNullablePref
import com.chibatching.kotpref.gsonpref.gsonPref
import com.prytula.identifolib.entities.IdentifoUser


/*
 * Created by Eugene Prytula on 2/25/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

interface IUserStorage {
    var user: IdentifoUser?
    fun clearAll()
}

class UserStorage(context: Context) : KotprefModel(context), IUserStorage {
    override var user: IdentifoUser? by gsonNullablePref()
    override fun clearAll() = clear()
}