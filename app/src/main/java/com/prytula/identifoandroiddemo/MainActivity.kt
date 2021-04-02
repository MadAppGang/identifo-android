package com.prytula.identifoandroiddemo

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.prytula.IdentifoAuthentication
import com.prytula.identifolib.entities.AuthState
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolibui.login.IdentifoSignInActivity
import com.prytula.identifolibui.login.options.*
import com.prytula.identifolibui.login.options.LoginProviders.*
import com.prytula.identifolibui.registration.IdentifoSingUpActivity
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val linearLayoutRoot by lazy { findViewById<LinearLayout>(R.id.leanerLayoutRoot) }
    private val textView by lazy { findViewById<TextView>(R.id.textState) }
    private val buttonLogin by lazy { findViewById<Button>(R.id.buttonSignIn) }
    private val buttonRegistration by lazy { findViewById<Button>(R.id.buttonSignUp) }
    private val buttonLogout by lazy { findViewById<Button>(R.id.buttonLogout) }
    private val checkBoxEmail by lazy { findViewById<CheckBox>(R.id.checkboxEmail) }
    private val checkBoxPhoneNumber by lazy { findViewById<CheckBox>(R.id.checkboxPhoneNumber) }
    private val checkBoxGoogle by lazy { findViewById<CheckBox>(R.id.checkboxGoogle) }
    private val checkBoxFacebook by lazy { findViewById<CheckBox>(R.id.checkboxFacebook) }
    private val checkBoxTwitter by lazy { findViewById<CheckBox>(R.id.checkboxTwitter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        val style = Style(
            companyLogo = R.drawable.ic_madappgang,
            companyName = getString(R.string.company_name),
            greetingsText = getString(R.string.company_greetings)
        )

        val userConditions = UseConditions(
            "https://madappgang.com/",
            "https://madappgang.com/experience"
        )

        buttonRegistration.setOnClickListener {
            IdentifoSingUpActivity.openActivity(this)
        }

        buttonLogin.setOnClickListener {
            val providers = mutableListOf<LoginProviders>()

            if (checkBoxEmail.isChecked) providers += EMAIL
            if (checkBoxPhoneNumber.isChecked) providers += PHONE
            if (checkBoxGoogle.isChecked) providers += GMAIL
            if (checkBoxFacebook.isChecked) providers += FACEBOOK
            if (checkBoxTwitter.isChecked) providers += TWITTER

            val loginOptions = LoginOptions(
                commonStyle = style,
                providers = providers,
                useConditions = userConditions
            )

            IdentifoSignInActivity.openActivity(this, loginOptions)
        }

        IdentifoAuthentication.authenticationState.asLiveData().observe(this) { state ->
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
    }
}