package com.prytula.identifolibui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.prytula.identifolibui.R


/*
 * Created by Eugene Prytula on 2/26/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class CommonLoginFragment : Fragment(R.layout.fragment_common_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.buttonLoginWithPhoneNumber).setOnClickListener {
            findNavController().navigate(R.id.action_commonLoginFragment_to_phoneNumberLoginFragment)
        }
    }
}