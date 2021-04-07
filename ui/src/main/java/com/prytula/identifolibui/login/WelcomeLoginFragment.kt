package com.prytula.identifolibui.login

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.prytula.identifolib.entities.FederatedProviders
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentWelcomeBinding
import com.prytula.identifolibui.extensions.*
import com.prytula.identifolibui.login.options.LoginOptions
import com.prytula.identifolibui.login.options.LoginProviders
import com.prytula.identifolibui.login.options.Style
import com.prytula.identifolibui.login.options.UseConditions
import com.twitter.sdk.android.core.*


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class WelcomeLoginFragment : Fragment(R.layout.fragment_welcome) {

    companion object {
        private const val RC_SIGN_IN = 1
    }

    private val welcomeBinding by viewBinding(FragmentWelcomeBinding::bind)
    private val commonViewModel: CommonViewModel by viewModels()

    private val loginOptions: LoginOptions by lazy { (requireActivity() as IdentifoSignInActivity).loginOptions }
    private val commonStyle: Style? by lazy { loginOptions.commonStyle }
    private val loginProviders: List<LoginProviders>? by lazy { loginOptions.providers }
    private val userConditions: UseConditions? by lazy { loginOptions.useConditions }

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

        welcomeBinding.constraintLoginRoot.addSystemTopBottomPadding()

        Twitter.initialize(requireContext())

        commonViewModel.finishSigningIn.asLiveData()
            .observe(viewLifecycleOwner) { federatedResponse ->
                requireActivity().finish()
            }

        commonViewModel.receiveError.asLiveData().observe(viewLifecycleOwner) { errorResponse ->
            welcomeBinding.constraintLoginRoot.showMessage(errorResponse.error.message)
        }

        commonStyle?.let {
            it.backgroundRes?.let {
                welcomeBinding.constraintLoginRoot.setBackgroundResource(it)
            }

            welcomeBinding.imageViewLogo.load(it.companyLogo)
            welcomeBinding.textViewCompanyName.text = it.companyName
            welcomeBinding.textViewCompanyGreetings.text = it.greetingsText
        }

        loginProviders?.let { providers ->
            if (providers.isEmpty()) {
                throw Exception("You need to have at least one provider!")
            }

            val allTypesIdentifiers = providers.any { !it.isAuxiliaryIdentifier } and providers.any { it.isAuxiliaryIdentifier }
            if (allTypesIdentifiers) {
                welcomeBinding.viewSeparator.visibility = View.VISIBLE
            }

            if (LoginProviders.GMAIL in providers) {
                with(welcomeBinding.buttonLoginWithGoogle) {
                    visibility = View.VISIBLE
                    setOnClickListener { signInWithGoogle() }
                }
            }

            if (LoginProviders.EMAIL in providers) {
                with(welcomeBinding.buttonLoginWithUsername) {
                    visibility = View.VISIBLE
                    setOnClickListener { findNavController().navigate(R.id.action_commonLoginFragment_to_usernameLoginFragment) }
                }
            }

            if (LoginProviders.PHONE in providers) {
                with(welcomeBinding.buttonLoginWithPhoneNumber) {
                    visibility = View.VISIBLE
                    setOnClickListener { findNavController().navigate(R.id.action_commonLoginFragment_to_phoneNumberLoginFragment) }
                }
            }

            if (LoginProviders.FACEBOOK in providers) {
                setupFacebookCallback()
                with(welcomeBinding.buttonLoginWithFacebook) {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        welcomeBinding.buttonLoginWithFacebookNative.performClick()
                    }
                }
            }

            if (LoginProviders.TWITTER in providers) {
                setupTwitterCallback()
                with(welcomeBinding.buttonLoginWithTwitter) {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        welcomeBinding.buttonLoginWithTwitterNative.performClick()
                    }
                }
            }
        }

        userConditions?.let { userConditions ->
            val userAgreementText = getString(R.string.userAgreement)
                .makeUrl(userConditions.userAgreement)
                .makeAnotherColor(ContextCompat.getColor(requireContext(), R.color.identifoAccentColorPrimary))

            val privacyPolicy = getString(R.string.privacyPolicy)
                .makeUrl(userConditions.privacyPolicy)
                .makeAnotherColor(ContextCompat.getColor(requireContext(), R.color.identifoAccentColorPrimary))

            val userAgreementNotice = getString(R.string.userAgreementNotice)
                .makeSpannableString() + userAgreementText + getString(R.string.userAgreementNoticeAnd).makeSpannableString() + privacyPolicy

            welcomeBinding.textViewUserAgreement.run {
                visibility = View.VISIBLE
                movementMethod = LinkMovementMethod.getInstance()
                text = userAgreementNotice
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        welcomeBinding.buttonLoginWithTwitterNative.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun setupFacebookCallback() {
        welcomeBinding.buttonLoginWithFacebookNative.setPermissions(
            "email",
            "public_profile"
        )
        welcomeBinding.buttonLoginWithFacebookNative.fragment = this
        welcomeBinding.buttonLoginWithFacebookNative.registerCallback(
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
                    welcomeBinding.constraintLoginRoot.showMessage("${error?.message}")
                }
            })
    }

    private fun setupTwitterCallback() {
        welcomeBinding.buttonLoginWithTwitterNative.callback =
            object : Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>?) {
                    val session = TwitterCore.getInstance().sessionManager.activeSession
                    val authToken = session.authToken
                    commonViewModel.sendFederatedToken(
                        FederatedProviders.TWITTER, authToken.token
                    )
                }

                override fun failure(exception: TwitterException?) {
                    welcomeBinding.constraintLoginRoot.showMessage("${exception?.message}")
                }
            }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken: String = account?.idToken ?: ""
            commonViewModel.sendFederatedToken(FederatedProviders.GOOGLE, idToken)
        } catch (e: Exception) {
            welcomeBinding.constraintLoginRoot.showMessage(e.message.toString())
        }
    }

    private fun signInWithGoogle() {
        val intent = googleClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }
}