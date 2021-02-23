package com.prytula.identifolibui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 2/17/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class IdentifoActivity : AppCompatActivity() {

    companion object {
        fun openActivity(context: Context) {
            context.startActivity<IdentifoActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identifo)

        val editTextUsername = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val editTextPassword = findViewById<EditText>(R.id.editTextNumberPassword)
        findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            loginWithUsernameAndPassword(editTextUsername.text.toString(), editTextPassword.text.toString())
        }
    }

    private fun loginWithUsernameAndPassword(username : String, password : String) {
        lifecycleScope.launch {
            IdentifoAuth.registerWithUsernameAndPassword(username, password, true).onSuccess {
                finish()
            }.onError {
                showMessage("Error - $it")
            }
        }
    }
}