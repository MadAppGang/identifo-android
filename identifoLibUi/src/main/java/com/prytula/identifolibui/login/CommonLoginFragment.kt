package com.prytula.identifolibui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.R
import com.prytula.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class CommonLoginFragment : Fragment(R.layout.fragment_common_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rootView = view.findViewById<ConstraintLayout>(R.id.constraint_login_root)
        val usernameEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextPassword)
        val loginButton = view.findViewById<Button>(R.id.buttonLogin)
        val loginWithPhoneNumber = view.findViewById<Button>(R.id.buttonLoginWithPhoneNumber)

        loginButton.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()

                IdentifoAuth.loginWithUsernameAndPassword(username, password).onError {
                    rootView.showMessage(it.error.message)
                }.onSuccess {
                    requireActivity().finish()
                }
            }
        }

        loginWithPhoneNumber.setOnClickListener {
            findNavController().navigate(R.id.action_commonLoginFragment_to_phoneNumberLoginFragment)
        }
    }
}