package com.prytula.identifolibui.registration

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.prytula.identifolibui.R
import com.prytula.identifolibui.extensions.startActivity


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
        window.decorView.rootView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}