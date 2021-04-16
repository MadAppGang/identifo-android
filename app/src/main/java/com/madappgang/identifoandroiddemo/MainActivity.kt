package com.madappgang.identifoandroiddemo

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.madappgang.IdentifoAuthentication
import com.madappgang.identifolib.entities.AuthState
import com.madappgang.identifolib.extensions.onError
import com.madappgang.identifolibui.login.IdentifoSignInActivity
import com.madappgang.identifolibui.login.options.LoginOptions
import com.madappgang.identifolibui.login.options.LoginProviders
import com.madappgang.identifolibui.login.options.LoginProviders.*
import com.madappgang.identifolibui.login.options.Style
import com.madappgang.identifolibui.login.options.UseConditions
import com.madappgang.identifolibui.registration.IdentifoSingUpActivity
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}