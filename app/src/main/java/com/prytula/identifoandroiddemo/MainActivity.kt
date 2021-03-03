package com.prytula.identifoandroiddemo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.prytula.IdentifoAuth
import com.prytula.identifolibui.login.options.FacebookLoginOption
import com.prytula.identifolibui.login.IdentifoLoginActivity
import com.prytula.identifolibui.login.options.GoogleLoginOption
import com.prytula.identifolibui.login.options.LoginOptions
import com.prytula.identifolibui.login.options.PhoneNumberOption
import com.prytula.identifolibui.registration.IdentifoRegistrationActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val textView by lazy { findViewById<TextView>(R.id.textState) }
    private val buttonRegister by lazy { findViewById<Button>(R.id.buttonRegister) }
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

        buttonRegister.setOnClickListener { IdentifoRegistrationActivity.openActivity(this) }

        buttonLogin.setOnClickListener {
            val loginOptions = LoginOptions(
                phoneNumberOption = PhoneNumberOption(),
                googleLoginOption = GoogleLoginOption("153119406654-a2uoase99mdskhsrs406v5g7l9bp7dvc.apps.googleusercontent.com"),
                facebookLoginOption = FacebookLoginOption()
            )
            IdentifoLoginActivity.openActivity(this, loginOptions)
        }

        IdentifoAuth.authState.asLiveData().observe(this) { authentificationState ->
            textView.text = authentificationState.toString()
        }
    }
}