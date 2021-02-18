package com.prytula.identifoandroiddemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.prytula.IdentifoAuth
import com.prytula.identifolib.entities.AuthState
import com.prytula.identifolibui.IdentifoActivity
import com.prytula.identifolibui.showMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val textView by lazy { findViewById<TextView>(R.id.textState) }
    private val buttonLogin by lazy { findViewById<Button>(R.id.buttonLogin) }
    private val buttonLogout by lazy { findViewById<Button>(R.id.buttonLogout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appID = "bk9o707k3t4c72q2qqqq"
        val secret = "vUYvSt8rEI7lTPIM96MMwPS3"
        val baseUrl = "https://identifo.jackrudenko.com"

        IdentifoAuth.initAuthenticator(applicationContext, baseUrl, appID, secret)

        buttonLogout.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                IdentifoAuth.logout()
            }
        }

        buttonLogin.setOnClickListener {
            IdentifoActivity.openActivity(this)
        }

        IdentifoAuth.authState.asLiveData().observe(this) { authentificationState ->
            textView.text = authentificationState.toString()
        }
    }
}