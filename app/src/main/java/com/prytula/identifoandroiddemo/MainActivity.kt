package com.prytula.identifoandroiddemo

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.prytula.IdentifoAuthentication
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolibui.login.IdentifoActivity
import com.prytula.identifolibui.login.options.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val linearLayoutRoot by lazy { findViewById<LinearLayout>(R.id.leanerLayoutRoot) }
    private val textView by lazy { findViewById<TextView>(R.id.textState) }
    private val buttonLogin by lazy { findViewById<Button>(R.id.buttonLogin) }
    private val buttonLogout by lazy { findViewById<Button>(R.id.buttonLogout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogout.setOnClickListener {
            lifecycleScope.launch {
                IdentifoAuthentication.logout().onError { errorResponse ->
                    Snackbar.make(linearLayoutRoot, errorResponse.error.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        buttonLogin.setOnClickListener {
            val providers = listOf(
                LoginProviders.GMAIL,
                LoginProviders.TWITTER,
                LoginProviders.FACEBOOK,
                LoginProviders.PHONE,
                LoginProviders.EMAIL
            )
            val style = Style(
                companyLogo = R.drawable.ic_launcher_background,
                companyName = getString(R.string.company_name),
                greetingsText = getString(R.string.company_greetings)
            )

            val userConditions = UseConditions(
                "https://madappgang.com/",
                "https://madappgang.com/experience"
            )

            val loginOptions = LoginOptions(
                commonStyle = style,
                providers = providers,
                userConditions
            )
            IdentifoActivity.openActivity(this, loginOptions)
        }

        IdentifoAuthentication.authenticationState.asLiveData().observe(this) { authentificationState ->
            textView.text = authentificationState.toString()
        }
    }
}