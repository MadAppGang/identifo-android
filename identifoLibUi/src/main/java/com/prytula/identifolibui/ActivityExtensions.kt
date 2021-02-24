package com.prytula.identifolibui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast


/*
 * Created by Eugene Prytula on 2/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

internal inline fun <reified T : Activity> Context.startActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}