package com.prytula.identifolibui

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
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
        fun openActivity(context: Context) {
            context.startActivity<IdentifoLoginActivity>()
        }
    }

    private val rootView by lazy { findViewById<ConstraintLayout>(R.id.constraint_registration_root) }
    private val buttonLogin by lazy { findViewById<Button>(R.id.buttonLogin) }
    private val editTextUsername by lazy { findViewById<EditText>(R.id.editTextTextEmailAddress) }
    private val editTextPassword by lazy { findViewById<EditText>(R.id.editTextPassword) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identifo_login)

        buttonLogin.setOnClickListener {
            loginWithUsernameAndPassword(
                editTextUsername.text.toString(),
                editTextPassword.text.toString()
            )
        }

        editTextPassword.onDone {
            loginWithUsernameAndPassword(
                editTextUsername.text.toString(),
                editTextPassword.text.toString()
            )
        }
    }

    private fun loginWithUsernameAndPassword(username: String, password: String) {
        lifecycleScope.launch {
            IdentifoAuth.loginWithUsernameAndPassword(username, password).onSuccess {
                finish()
            }.onError {
                rootView.showMessage(it.error.message)
            }
        }
    }
}