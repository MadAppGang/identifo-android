package com.prytula.identifolibui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
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
import com.prytula.identifolibui.login.options.*
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

    private val loginOptions: LoginOptions by lazy { (requireActivity() as IdentifoLoginActivity).loginOptions }
    private val commonStyle: CommonStyle? by lazy { loginOptions.commonStyle }
    private val loginProviders: List<LoginProviders>? by lazy { loginOptions.providers }

    private val googleOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_api_key))
            .requestEmail()
            .build()
    }

    private val googleClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
            requireActivity(),
            googleOptions
        )
    }

    private val facebookCallbackManager by lazy { CallbackManager.Factory.create() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView = view.findViewById(R.id.constraint_login_root)

        val loginWithPhoneNumber = view.findViewById<Button>(R.id.buttonLoginWithPhoneNumber)
        val loginWithGoogle = view.findViewById<Button>(R.id.buttonLoginWithGoogle)
        val loginWithFacebookNative =
            view.findViewById<LoginButton>(R.id.buttonLoginWithFacebookNative)
        val loginWithFacebook = view.findViewById<Button>(R.id.buttonLoginWithFacebook)
        val imageLogo = view.findViewById<ImageView>(R.id.imageViewLogo)
        val loginWithEmail = view.findViewById<Button>(R.id.buttonLoginWithUsername)

        commonStyle?.let {
            it.imageRes?.let {
                imageLogo.setImageResource(it)
            }
        }

        loginProviders?.let {
            if (it.contains(LoginProviders.GMAIL)) {
                loginWithGoogle.visibility = View.VISIBLE
                loginWithGoogle.setOnClickListener { signIn() }
            }

            if (it.contains(LoginProviders.EMAIL)) {
                loginWithEmail.visibility = View.VISIBLE
                loginWithEmail.setOnClickListener { findNavController().navigate(R.id.action_commonLoginFragment_to_usernameLoginFragment) }
            }

            if (it.contains(LoginProviders.PHONE)) {
                loginWithPhoneNumber.visibility = View.VISIBLE
                loginWithPhoneNumber.setOnClickListener { findNavController().navigate(R.id.action_commonLoginFragment_to_phoneNumberLoginFragment) }
            }

            if (it.contains(LoginProviders.FACEBOOK)) {
                loginWithFacebook.setOnClickListener {
                    loginWithFacebookNative.setPermissions("email", "public_profile")
                    loginWithFacebookNative.fragment = this
                    loginWithFacebookNative.registerCallback(
                        facebookCallbackManager,
                        object : FacebookCallback<LoginResult> {
                            override fun onSuccess(result: LoginResult?) {
                                sendFederatedToken(
                                    FederatedProviders.FACEBOOK,
                                    result?.accessToken?.token ?: ""
                                )
                            }

                            override fun onCancel() {
                                rootView.showMessage("Faceboock auth has been canceled")
                            }

                            override fun onError(error: FacebookException?) {
                                rootView.showMessage("Error - ${error?.message}")
                            }
                        })
                    loginWithFacebookNative.performClick()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
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