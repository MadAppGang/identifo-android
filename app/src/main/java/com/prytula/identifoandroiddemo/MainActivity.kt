package com.prytula.identifoandroiddemo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.entities.AuthState
import com.prytula.identifolibui.IdentifoActivity
import com.prytula.identifolibui.showMessage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appID = "bk9o707k3t4c72q2qqqq"
        val secret = "vUYvSt8rEI7lTPIM96MMwPS3"
        val baseUrl = "https://identifo.jackrudenko.com"

        IdentifoAuth.initAuthenticator(applicationContext, baseUrl, appID, secret)

        findViewById<Button>(R.id.buttonLogout).setOnClickListener {
            lifecycleScope.launchWhenCreated {
                IdentifoAuth.logout()
            }
        }

        IdentifoAuth.authState.asLiveData().observe(this) { authentificationState ->
            showMessage("$authentificationState")
            when (authentificationState) {
                AuthState.Deauthentificated -> IdentifoActivity.openActivity(this)
            }
        }
    }
}