package com.prytula.identifolibui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolibui.FederatedProviders
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentCommonLoginBinding
import com.prytula.identifolibui.databinding.FragmentLoginUsernameBinding
import com.prytula.identifolibui.extensions.showMessage
import com.prytula.identifolibui.login.options.Style
import com.prytula.identifolibui.login.options.LoginOptions
import com.prytula.identifolibui.login.options.LoginProviders


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class CommonLoginFragment : Fragment(R.layout.fragment_common_login) {

    companion object {
        private const val RC_SIGN_IN = 1
    }

    private val commonLoginBinding by viewBinding(FragmentCommonLoginBinding::bind)
    private val commonViewModel: CommonViewModel by viewModels()

    private val loginOptions: LoginOptions by lazy { (requireActivity() as IdentifoLoginActivity).loginOptions }
    private val commonStyle: Style? by lazy { loginOptions.commonStyle }
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

        commonViewModel.finishSigningIn.asLiveData()
            .observe(viewLifecycleOwner) { federatedResponse ->
                requireActivity().finish()
            }

        commonViewModel.receiveError.asLiveData().observe(viewLifecycleOwner) { errorResponse ->
            commonLoginBinding.constraintLoginRoot.showMessage(errorResponse.error.message)
        }

        commonStyle?.let {
            it.imageRes?.let {
                commonLoginBinding.imageViewLogo.setImageResource(it)
            }
            it.backgroundRes?.let {
                commonLoginBinding.constraintLoginRoot.setBackgroundResource(it)
            }
        }

        loginProviders?.let {
            if (it.isEmpty()) {
                throw Exception("You need to have at least one provider!")
            }

            if (it.contains(LoginProviders.GMAIL)) {
                commonLoginBinding.buttonLoginWithGoogle.run {
                    visibility = View.VISIBLE
                    setOnClickListener { signIn() }
                }
            }

            if (it.contains(LoginProviders.EMAIL)) {
                commonLoginBinding.buttonLoginWithUsername.run {
                    visibility = View.VISIBLE
                    setOnClickListener { findNavController().navigate(R.id.action_commonLoginFragment_to_usernameLoginFragment) }
                }
            }

            if (it.contains(LoginProviders.PHONE)) {
                commonLoginBinding.buttonLoginWithPhoneNumber.run {
                    visibility = View.VISIBLE
                    setOnClickListener { findNavController().navigate(R.id.action_commonLoginFragment_to_phoneNumberLoginFragment) }
                }
            }

            if (it.contains(LoginProviders.FACEBOOK)) {
                commonLoginBinding.buttonLoginWithFacebook.run {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        commonLoginBinding.buttonLoginWithFacebookNative.setPermissions(
                            "email",
                            "public_profile"
                        )
                        commonLoginBinding.buttonLoginWithFacebookNative.fragment =
                            this@CommonLoginFragment
                        commonLoginBinding.buttonLoginWithFacebookNative.registerCallback(
                            facebookCallbackManager,
                            object : FacebookCallback<LoginResult> {
                                override fun onSuccess(result: LoginResult?) {
                                    commonViewModel.sendFederatedToken(
                                        FederatedProviders.FACEBOOK,
                                        result?.accessToken?.token ?: ""
                                    )
                                }

                                override fun onCancel() {}

                                override fun onError(error: FacebookException?) {
                                    rootView.showMessage("${error?.message}")
                                }
                            })
                        commonLoginBinding.buttonLoginWithFacebookNative.performClick()
                    }
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
            commonViewModel.sendFederatedToken(FederatedProviders.GOOGLE, idToken)
        } catch (e: Exception) {
            commonLoginBinding.constraintLoginRoot.showMessage(e.message.toString())
        }
    }

    private fun signIn() {
        val intent = googleClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }
}