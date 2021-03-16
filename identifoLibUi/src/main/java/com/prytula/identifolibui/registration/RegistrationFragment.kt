package com.prytula.identifolibui.registration

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.CircleCropTransformation
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.FragmentRegistrationBinding
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage


/*
 * Created by Eugene Prytula on 3/15/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    companion object {
        private const val MIMETYPE_IMAGES = "image/*"
    }

    private val registrationBinding by viewBinding(FragmentRegistrationBinding::bind)
    private val registrationViewModel by viewModels<RegistrationViewModel>()

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            imageUri?.let {
                registrationBinding.imageViewAccount.load(it) {
                    crossfade(true)
                    placeholder(R.drawable.ic_account)
                    transformations(CircleCropTransformation())
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationBinding.buttonRegister.setOnClickListener { pushUsernameAndPassword() }
        registrationBinding.editTextPassword.onDone { pushUsernameAndPassword() }

        registrationViewModel.registrationSuccessful.asLiveData()
            .observe(viewLifecycleOwner) { registerResponse ->
                requireActivity().finish()
            }

        registrationViewModel.receiveError.asLiveData()
            .observe(viewLifecycleOwner) { errorResponse ->
                registrationBinding.constraintRegistrationRoot.showMessage(errorResponse.error.message)
            }

        registrationBinding.imageViewAccount.setOnClickListener {
            getImageContent.launch(MIMETYPE_IMAGES)
        }
    }

    private fun pushUsernameAndPassword() {
        val login = registrationBinding.editTextTextEmailAddress.text.toString()
        val password = registrationBinding.editTextPassword.text.toString()
        val repeatPassword = registrationBinding.editTextPasswordRepeat.text.toString()

        if (password == repeatPassword) {
            registrationViewModel.registerWithUsernameAndPassword(login, password)
        } else {
            registrationBinding.constraintRegistrationRoot.showMessage(getString(R.string.passwordsDoNotMatch))
        }
    }
}