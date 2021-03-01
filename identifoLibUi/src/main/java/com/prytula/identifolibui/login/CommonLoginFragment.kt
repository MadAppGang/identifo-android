package com.prytula.identifolibui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.FederatedProviders
import com.prytula.identifolibui.R
import com.prytula.identifolibui.extensions.showMessage
import java.lang.Exception


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class CommonLoginFragment : Fragment(R.layout.fragment_common_login) {

    companion object {
        private const val RC_SIGN_IN = 1
    }

    private lateinit var rootView: ConstraintLayout

    private val googleOptions: GoogleSignInOptions by lazy {
        // TODO: Add passing of app id
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("153119406654-a2uoase99mdskhsrs406v5g7l9bp7dvc.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

    private val googleClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
            requireActivity(),
            googleOptions
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView = view.findViewById(R.id.constraint_login_root)
        val usernameEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextPassword)
        val loginButton = view.findViewById<Button>(R.id.buttonLogin)
        val loginWithPhoneNumber = view.findViewById<Button>(R.id.buttonLoginWithPhoneNumber)
        val loginWithGoogle = view.findViewById<SignInButton>(R.id.buttonLoginWithGoogle)

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

        loginWithGoogle.setOnClickListener {
            signIn()
        }

        loginWithPhoneNumber.setOnClickListener {
            findNavController().navigate(R.id.action_commonLoginFragment_to_phoneNumberLoginFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken: String = account?.idToken ?: ""
            sendFederatedToken(FederatedProviders.GOOGLE, idToken)
        } catch (e: Exception) {
            rootView.showMessage(e.message.toString())
        }
    }

    private fun signIn() {
        val intent = googleClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    private fun sendFederatedToken(federatedProvider: FederatedProviders, token: String) {
        lifecycleScope.launchWhenCreated {
            IdentifoAuth.federatedLogin(federatedProvider.title, token).onSuccess {
                requireActivity().finish()
            }.onError {
                rootView.showMessage(it.error.message)
            }
        }
    }
}