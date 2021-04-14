package com.madappgang

import android.content.Context
import com.madappgang.identifolib.di.dependenciesModule
import com.madappgang.identifolib.entities.*
import com.madappgang.identifolib.entities.deanonymize.DeanonimizeDataSet
import com.madappgang.identifolib.entities.deanonymize.DeanonimizeResponse
import com.madappgang.identifolib.entities.federatedLogin.FederatedLoginDataSet
import com.madappgang.identifolib.entities.federatedLogin.FederatedLoginResponse
import com.madappgang.identifolib.entities.logging.LoginDataSet
import com.madappgang.identifolib.entities.logging.LoginResponse
import com.madappgang.identifolib.entities.logout.LogoutDataSet
import com.madappgang.identifolib.entities.phoneLogin.PhoneLoginDataSet
import com.madappgang.identifolib.entities.phoneLogin.PhoneLoginResponse
import com.madappgang.identifolib.entities.register.RegisterDataSet
import com.madappgang.identifolib.entities.register.RegisterResponse
import com.madappgang.identifolib.entities.requestCode.RequestPhoneCodeDataSet
import com.madappgang.identifolib.entities.requestCode.RequestPhoneCodeResponse
import com.madappgang.identifolib.entities.reserPassword.ResetPasswordDataSet
import com.madappgang.identifolib.entities.reserPassword.ResetPasswordResponse
import com.madappgang.identifolib.extensions.Result
import com.madappgang.identifolib.extensions.onSuccess
import com.madappgang.identifolib.extensions.suspendApiCall
import com.madappgang.identifolib.network.QueriesService
import com.madappgang.identifolib.network.interceptors.IdentifoAuthInterceptor
import com.madappgang.identifolib.network.interceptors.IdentifoRefreshAuthenticator
import com.madappgang.identifolib.storages.ITokenDataStorage
import com.madappgang.identifolib.storages.IUserStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin


/*
 * Created by Eugene Prytula on 2/5/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

object IdentifoAuthentication : KoinComponent {

    private val tokenDataStorage by inject<ITokenDataStorage>()
    private val userStorage by inject<IUserStorage>()
    private val queriesService by inject<QueriesService>()
    private val backgroundCoroutineDispatcher by inject<CoroutineDispatcher>()
    private val identifoAuthInterceptor by inject<IdentifoAuthInterceptor>()
    private val identifoRefreshAuthenticator by inject<IdentifoRefreshAuthenticator>()

    private val _authenticationState by lazy { MutableStateFlow(getInitialAuthentificationState()) }
    val authenticationState by lazy { _authenticationState.asStateFlow() }

    /**
     * The initial function to initialize the Identifo library.
     * Pay attention that this function must be called only in the Application class.
     *
     * @param context The application context.
     * @param baseUrl Identifo authentication URL.
     * @param applicationId The application access identifier.
     * @param secretKey The HMAC shared secret key.
     */

    fun initAuthenticator(
        context: Context,
        baseUrl: String,
        applicationId: String,
        secretKey: String
    ) {
        startKoin {
            androidContext(context)
            modules(
                dependenciesModule(
                    appId = applicationId,
                    appSecret = secretKey,
                    baseUrl = baseUrl
                )
            )
        }
    }

    private fun getInitialAuthentificationState(): AuthState {
        val tokens = tokenDataStorage.getTokens()
        val user = userStorage.getUser()
        val refreshToken = tokens.refresh
        return if (refreshToken.isExpired()) {
            AuthState.Deauthentificated
        } else {
            val accessToken = tokens.access?.jwtEncoded
            AuthState.Authentificated(user, accessToken)
        }
    }

    internal fun saveTokens(
        accessToken: String,
        refreshToken: String,
        user: IdentifoUser? = userStorage.getUser()
    ) {
        tokenDataStorage.setTokens(
            Tokens(
                Token.Access(accessToken),
                Token.Refresh(refreshToken)
            )
        )
        user?.let { userStorage.setUser(it) }
        _authenticationState.value = AuthState.Authentificated(user, accessToken)
    }

    internal fun clearTokens() {
        tokenDataStorage.clearAll()
        userStorage.clearAll()
        _authenticationState.value = AuthState.Deauthentificated
    }

    fun getIdentifoInterceptor() = identifoAuthInterceptor

    fun getIdentifoAuthenticator() = identifoRefreshAuthenticator


    /**
     * Provides the ability to register the new user via username/email and password, also defines a method of anonymity.
     *
     * @param username The login/mail of the new user.
     * @param password The new user's password.
     * @param isAnonymous Flag which determines the way of anonymity.
     *
     * @return If result of asynchronous operation is successful the suspend function returns us [RegisterResponse] with tokens and user's information,
     * otherwise it returns [ErrorResponse] with error information.
     */

    suspend fun registerWithUsernameAndPassword(
        username: String,
        password: String,
        isAnonymous: Boolean
    ): Result<RegisterResponse, ErrorResponse> = withContext(backgroundCoroutineDispatcher) {
        val registerCredentials =
            RegisterDataSet(username = username, password = password, anonymous = isAnonymous)
        return@withContext suspendApiCall {
            queriesService.registerWithUsernameAndPassword(registerCredentials)
        }.onSuccess {
            val fetchedUser = it.registeredUser
            val identifoUser = IdentifoUser(fetchedUser.id, fetchedUser.username, isAnonymous)
            saveTokens(it.accessToken, it.refreshToken, identifoUser)
        }
    }


    /**
     * The suspend function which helps to de-anonymize a anonymous user.
     *
     * @param oldUsername The anonymous user's username.
     * @param oldPassword The anonymous user's password.
     * @param newUsername The de-anonymized user's username.
     * @param newPassword The de-anonymized user's password.
     *
     * @return If result of asynchronous operation is successful the suspend function returns us [DeanonimizeResponse] with confirmation of operation,
     * otherwise it returns [ErrorResponse] with error information.
     */

    suspend fun deanonymizeUser(
        oldUsername: String,
        oldPassword: String,
        newUsername: String,
        newPassword: String
    ): Result<DeanonimizeResponse, ErrorResponse> = withContext(backgroundCoroutineDispatcher) {
        val deanonimizeDataSet = DeanonimizeDataSet(
            oldUsername,
            oldPassword,
            newUsername,
            newPassword
        )
        return@withContext suspendApiCall { queriesService.deanonymize(deanonimizeDataSet) }
    }


    /**
     * Provides the ability to sign in via username/email and password.
     *
     * @param username The login/mail of the registered user.
     * @param password The registered user's password.
     *
     * @return If result of asynchronous operation is successful the suspend function returns us [LoginResponse] with tokens and user's information,
     * otherwise it returns [ErrorResponse] with error information.
     */

    suspend fun loginWithUsernameAndPassword(
        username: String,
        password: String
    ): Result<LoginResponse, ErrorResponse> = withContext(backgroundCoroutineDispatcher) {
        val loginDataSet = LoginDataSet(username, password)
        return@withContext suspendApiCall {
            queriesService.loginWithUsernameAndPassword(loginDataSet)
        }.onSuccess {
            val fetchedUser = it.loggedUser
            val identifoUser = IdentifoUser(fetchedUser.id, fetchedUser.username, false)
            saveTokens(it.accessToken, it.refreshToken, identifoUser)
        }
    }


    /**
     * Starts sending of the one time password to the certain phone number.
     *
     * @param phoneNumber The user's phone number.
     *
     * @return If result of asynchronous operation is successful the suspend function returns us [RequestPhoneCodeResponse] with confirmation of operation,
     * otherwise it returns [ErrorResponse] with error information.
     *
     */

    suspend fun requestPhoneCode(phoneNumber: String): Result<RequestPhoneCodeResponse, ErrorResponse> =
        withContext(backgroundCoroutineDispatcher) {
            val requestPhoneCodeDataSet = RequestPhoneCodeDataSet(phoneNumber)
            return@withContext suspendApiCall {
                queriesService.requestPhoneCode(
                    requestPhoneCodeDataSet
                )
            }
        }


    /**
     * Sign in via the phone number and one time password.
     *
     * @param phoneNumber The user's phone number.
     * @param oneTimePassword The fetched one time password.
     *
     * @return If result of asynchronous operation is successful the suspend function returns us [PhoneLoginResponse] with tokens and user's information,
     * otherwise it returns [ErrorResponse] with error information.
     *
     */

    suspend fun phoneLogin(
        phoneNumber: String,
        oneTimePassword: String
    ): Result<PhoneLoginResponse, ErrorResponse> = withContext(backgroundCoroutineDispatcher) {
        val loginDataSet = PhoneLoginDataSet(phoneNumber, oneTimePassword)
        return@withContext suspendApiCall { queriesService.phoneLogin(loginDataSet) }.onSuccess {
            val fetchedUser = it.loggedUser
            val identifoUser = IdentifoUser(fetchedUser.id, fetchedUser.username, false)
            saveTokens(it.accessToken, it.refreshToken, identifoUser)
        }
    }


    /**
     * Starts recovering password by sending the new temporal password to the user's email.
     *
     * @param email The user's email address.
     *
     * @return If result of asynchronous operation is successful the suspend function returns us [ResetPasswordResponse] with confirmation of operation,
     * otherwise it returns [ErrorResponse] with error information.
     *
     */

    suspend fun resetPassword(email: String): Result<ResetPasswordResponse, ErrorResponse> =
        withContext(backgroundCoroutineDispatcher) {
            val resetPasswordDataSet = ResetPasswordDataSet(email)
            return@withContext suspendApiCall { queriesService.resetPassword(resetPasswordDataSet) }
        }


    /**
     * Authenticates user via certain identity provider (Facebook, Google, Twitter etc.).
     *
     * @param provider Title of identity provider. Currently available providers you can see here [FederatedProviders].
     * @param token Fetched identity access token.
     *
     * @return If result of asynchronous operation is successful the suspend function returns us [FederatedLoginResponse] with tokens and user's information,
     * otherwise it returns [ErrorResponse] with error information.
     *
     */

    suspend fun federatedLogin(
        provider: String,
        token: String
    ): Result<FederatedLoginResponse, ErrorResponse> = withContext(backgroundCoroutineDispatcher) {
        val federatedLoginDataSet = FederatedLoginDataSet(provider, token)
        return@withContext suspendApiCall { queriesService.federatedLogin(federatedLoginDataSet) }.onSuccess {
            val fetchedUser = it.loggedUser
            val identifoUser = IdentifoUser(fetchedUser.id, fetchedUser.username, false)
            saveTokens(it.accessToken, it.refreshToken, identifoUser)
        }
    }

    /**
     * Performs logging out.
     *
     * @return If result of asynchronous operation is successful the suspend function returns us [Result.Success] without any data,
     * otherwise it returns [ErrorResponse] with error information.
     *
     */

    suspend fun logout(): Result<Unit, ErrorResponse> = withContext(backgroundCoroutineDispatcher) {
        val refreshToken = tokenDataStorage.getTokens().refresh?.jwtEncoded ?: ""
        return@withContext suspendApiCall { queriesService.logout(LogoutDataSet(refreshToken)) }.onSuccess {
            clearTokens()
        }
    }
}
