package com.madappgang.identifoandroiddemo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.madappgang.IdentifoAuthentication
import com.madappgang.identifolib.entities.AuthState
import com.madappgang.identifolib.extensions.onError
import com.madappgang.identifolibui.login.IdentifoSignInActivity
import com.madappgang.identifolibui.login.options.LoginOptions
import com.madappgang.identifolibui.login.options.LoginProviders
import com.madappgang.identifolibui.login.options.Style
import com.madappgang.identifolibui.login.options.UseConditions
import com.madappgang.identifolibui.registration.IdentifoSingUpActivity
import kotlinx.coroutines.launch


/*
 * Created by Eugene Prytula on 4/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class DemoFragment : Fragment(R.layout.fragment_demo) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutRoot by lazy { view.findViewById<LinearLayout>(R.id.leanerLayoutRoot) }
        val textView by lazy { view.findViewById<TextView>(R.id.textState) }
        val buttonLogin by lazy { view.findViewById<Button>(R.id.buttonSignIn) }
        val buttonRegistration by lazy { view.findViewById<Button>(R.id.buttonSignUp) }
        val buttonLogout by lazy { view.findViewById<Button>(R.id.buttonLogout) }
        val checkBoxEmail by lazy { view.findViewById<CheckBox>(R.id.checkboxEmail) }
        val checkBoxPhoneNumber by lazy { view.findViewById<CheckBox>(R.id.checkboxPhoneNumber) }
        val checkBoxGoogle by lazy { view.findViewById<CheckBox>(R.id.checkboxGoogle) }
        val checkBoxFacebook by lazy { view.findViewById<CheckBox>(R.id.checkboxFacebook) }
        val checkBoxTwitter by lazy { view.findViewById<CheckBox>(R.id.checkboxTwitter) }

        buttonLogout.setOnClickListener {
            lifecycleScope.launch {
                IdentifoAuthentication.logout().onError { errorResponse ->
                    Snackbar.make(
                        linearLayoutRoot,
                        errorResponse.error.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        IdentifoAuthentication.authenticationState.asLiveData()
            .observe(viewLifecycleOwner) { state ->
                when (state) {
                    is AuthState.Authentificated -> {
                        val accessToken = state.accessToken
                        val user = state.identifoUser
                        textView.text = "User - ${user}, token - $accessToken"
                    }
                    else -> {
                        textView.text = "Deauthenticated"
                    }
                }
            }

        val style = Style(
            companyLogo = R.drawable.ic_madappgang,
            companyName = getString(R.string.company_name),
            greetingsText = getString(R.string.company_greetings)
        )

        val userConditions = UseConditions(
            "https://madappgang.com/",
            "https://madappgang.com/experience"
        )

        buttonLogin.setOnClickListener {
            val providers = mutableListOf<LoginProviders>()

            if (checkBoxEmail.isChecked) providers += LoginProviders.EMAIL
            if (checkBoxPhoneNumber.isChecked) providers += LoginProviders.PHONE
            if (checkBoxGoogle.isChecked) providers += LoginProviders.GMAIL
            if (checkBoxFacebook.isChecked) providers += LoginProviders.FACEBOOK
            if (checkBoxTwitter.isChecked) providers += LoginProviders.TWITTER

            val loginOptions = LoginOptions(
                commonStyle = style,
                providers = providers,
                useConditions = userConditions
            )

            IdentifoSignInActivity.openActivity(requireContext(), loginOptions)
        }

        buttonRegistration.setOnClickListener {
            IdentifoSingUpActivity.openActivity(requireContext())
        }
    }
}