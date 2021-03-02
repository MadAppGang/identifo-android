package com.prytula.identifolibui.login

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.R
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage
import com.prytula.identifolibui.extensions.startActivity
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 2/24/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class IdentifoLoginActivity : AppCompatActivity() {

    companion object {
        private const val GOOGLE_APP_ID_KEY = "google_app_id_key"

        fun openActivity(context: Context, googleApiKey : String = "") {
            context.startActivity<IdentifoLoginActivity> {
                putExtra(GOOGLE_APP_ID_KEY, googleApiKey)
            }
        }
    }

    val googleApiKey by lazy { intent.extras?.getString(GOOGLE_APP_ID_KEY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identifo_login)
    }
}