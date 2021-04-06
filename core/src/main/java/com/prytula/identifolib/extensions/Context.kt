package com.prytula.identifolib.extensions

import android.content.Context


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

fun Context.readFileFromAssets(filePath: String): String {
    return resources.assets.open(filePath).bufferedReader().use {
        it.readText()
    }
}