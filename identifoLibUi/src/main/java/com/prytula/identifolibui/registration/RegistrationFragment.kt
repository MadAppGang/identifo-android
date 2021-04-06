package com.prytula.identifolibui.registration

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.CircleCropTransformation
import com.prytula.identifolib.entities.ErrorResponse
import com.prytula.identifolibui.R
import com.prytula.identifolibui.databinding.ActivitySignUpBinding
import com.prytula.identifolibui.databinding.FragmentRegistrationBinding
import com.prytula.identifolibui.extensions.addSystemTopBottomPadding
import com.prytula.identifolibui.extensions.onDone
import com.prytula.identifolibui.extensions.showMessage
import com.prytula.identifolibui.login.IdentifoSignInActivity


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

        registrationBinding.nestedScrollViewSignUpRoot.addSystemTopBottomPadding()

        registrationBinding.imageViewBackArrow.isVisible = requireActivity() is IdentifoSignInActivity
        registrationBinding.imageViewBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        registrationBinding.buttonRegister.setOnClickListener { pushUsernameAndPassword() }
        registrationBinding.editTextPassword.onDone { pushUsernameAndPassword() }


        registrationViewModel.registrationUIState.asLiveData()
            .observe(viewLifecycleOwner) { state ->
                when (state) {
                    RegistrationUIStates.Loading -> showLoading()
                    is RegistrationUIStates.RegistrationSuccessful -> closeActivity()
                    is RegistrationUIStates.RegistrationFailure -> showErrorMessage(state.error)
                    else -> hideLoading()
                }
            }

        registrationBinding.imageViewAccount.setOnClickListener {
            getImageContent.launch(MIMETYPE_IMAGES)
        }
    }

    private fun closeActivity() {
        hideLoading()
        requireActivity().finish()
    }

    private fun showErrorMessage(errorResponse: ErrorResponse) {
        hideLoading()
        registrationBinding.constraintRegistrationRoot.showMessage(errorResponse.error.message)
    }

    private fun showLoading() {
        registrationBinding.progressBarLine.show()
    }

    private fun hideLoading() {
        registrationBinding.progressBarLine.hide()
    }

    private fun pushUsernameAndPassword() {
        val login = registrationBinding.editTextUsername.text.toString()
        val password = registrationBinding.editTextPassword.text.toString()
        val repeatPassword = registrationBinding.editTextConfirmPassword.text.toString()

        if (password == repeatPassword) {
            registrationViewModel.registerWithUsernameAndPassword(login, password)
        } else {
            registrationBinding.constraintRegistrationRoot.showMessage(getString(R.string.passwordsDoNotMatch))
        }
    }
}