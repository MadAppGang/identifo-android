package com.prytula.identifolibui.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar


/*
 * Created by Eugene Prytula on 2/24/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */


internal fun View.showMessage(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}