package com.prytula.identifolib.entities


/*
 * Created by Eugene Prytula on 2/25/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

data class IdentifoUser(
    val id : String,
    val username : String,
    val isAnonymous : Boolean
)