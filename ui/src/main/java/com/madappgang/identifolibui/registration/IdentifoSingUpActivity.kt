package com.madappgang.identifolibui.registration

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.madappgang.identifolibui.R
import com.madappgang.identifolibui.extensions.startActivity


/*
 * Created by Eugene Prytula on 4/2/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class IdentifoSingUpActivity : AppCompatActivity() {

    companion object {
        fun openActivity(context: Context) {
            context.startActivity<IdentifoSingUpActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        window?.run {
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }
    }
}