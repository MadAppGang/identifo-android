package com.prytula.identifolibui.login.username

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.R
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 3/4/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class UsernameLoginFragment : Fragment(R.layout.fragment_login_username) {
    private lateinit var rootView: ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextPassword)
        val loginButton = view.findViewById<Button>(R.id.buttonLogin)

        rootView = view.findViewById(R.id.constraint_login_root)

        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        passwordEditText.onDone { performLogin(username, password) }
        loginButton.setOnClickListener { performLogin(username, password) }
    }

    private fun performLogin(username: String, password: String) {
        lifecycleScope.launchWhenCreated {
            IdentifoAuth.loginWithUsernameAndPassword(username, password).onError {
                rootView.showMessage(it.error.message)
            }.onSuccess {
                requireActivity().finish()
            }
        }
    }
}