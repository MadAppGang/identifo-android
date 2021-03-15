package com.prytula.identifoandroiddemo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.prytula.IdentifoAuth
import com.prytula.identifolibui.login.IdentifoActivity
import com.prytula.identifolibui.login.options.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val textView by lazy { findViewById<TextView>(R.id.textState) }
    private val buttonLogin by lazy { findViewById<Button>(R.id.buttonLogin) }
    private val buttonLogout by lazy { findViewById<Button>(R.id.buttonLogout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogout.setOnClickListener {
            lifecycleScope.launch {
                IdentifoAuth.logout()
            }
        }

        buttonLogin.setOnClickListener {
            val providers = listOf(
                LoginProviders.GMAIL,
                LoginProviders.FACEBOOK,
                LoginProviders.PHONE,
                LoginProviders.EMAIL
            )
            val style = Style(
                imageRes = R.drawable.ic_logo,
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

        IdentifoAuth.authState.asLiveData().observe(this) { authentificationState ->
            textView.text = authentificationState.toString()
        }
    }
}