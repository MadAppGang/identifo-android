package com.prytula

import android.content.Context
import com.prytula.identifolib.ITokenDataStorage
import com.prytula.identifolib.QueryManager
import com.prytula.identifolib.entities.*
import com.prytula.identifolib.entities.deanonymize.DeanonimizeDataSet
import com.prytula.identifolib.entities.deanonymize.DeanonimizeResponse
import com.prytula.identifolib.entities.federatedLogin.FederatedLoginDataSet
import com.prytula.identifolib.entities.federatedLogin.FederatedLoginResponse
import com.prytula.identifolib.entities.logging.LoginDataSet
import com.prytula.identifolib.entities.logging.LoginResponse
import com.prytula.identifolib.entities.phoneLogin.PhoneLoginDataSet
import com.prytula.identifolib.entities.phoneLogin.PhoneLoginResponse
import com.prytula.identifolib.entities.refreshToken.RefreshTokenResponse
import com.prytula.identifolib.entities.register.RegisterDataSet
import com.prytula.identifolib.entities.register.RegisterResponse
import com.prytula.identifolib.entities.requestCode.RequestPhoneCodeDataSet
import com.prytula.identifolib.entities.requestCode.RequestPhoneCodeResponse
import com.prytula.identifolib.extensions.Result
import com.prytula.identifolib.extensions.onError
import com.prytula.identifolib.extensions.onSuccess
import com.prytula.identifolib.extensions.suspendApiCall
import com.prytula.identifolib.network.QueriesManager
import com.prytula.identifolib.network.RefreshTokenManager
import com.prytula.identifolib.network.queries.QueriesService
import com.prytula.identifolib.network.queries.RefreshSessionQueries
import com.prytula.identifolib.storages.IStorageManager
import com.prytula.identifolib.storages.StorageManager
import com.prytula.workManagers.RefreshTokenWorkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/*
 * Created by Eugene Prytula on 2/5/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

object IdentifoAuth {

    private lateinit var storageManager: IStorageManager
    private val tokenDataStorage: ITokenDataStorage by lazy { storageManager.tokenManager() }

    private val queryManager: QueryManager<QueriesService> = QueriesManager()
    private val refreshTokenManager: QueryManager<RefreshSessionQueries> = RefreshTokenManager()

    private lateinit var queriesService: QueriesService
    private lateinit var refreshTokenService: RefreshSessionQueries
    private lateinit var context: Context

    private val _authState by lazy { MutableStateFlow(getInitialAuthentificationState()) }
    val authState by lazy { _authState.asStateFlow() }

    private fun setAuthState(authState: AuthState) {
        _authState.value = authState

        when (authState) {
            is AuthState.Authentificated -> {
                tokenDataStorage.setTokens(
                    Tokens(
                        Token.Access(authState.accessToken),
                        Token.Refresh(authState.refreshToken)
                    )
                )
                tokenDataStorage.setAnonymousState(authState.anonymousState)
                RefreshTokenWorkManager.startWorker(context)
            }
            AuthState.Deauthentificated -> {
                tokenDataStorage.clearAll()
                RefreshTokenWorkManager.cancel(context)
            }
        }
    }

    fun initAuthenticator(
        context: Context,
        baseUrl: String,
        appId: String,
        secretKey: String
    ) {
        this.context = context.applicationContext

        storageManager = StorageManager(context)
        queriesService = queryManager.getNetworkService(context, baseUrl, appId, secretKey)
        refreshTokenService = refreshTokenManager.getNetworkService(
            context,
            baseUrl,
            appId,
            secretKey,
            tokenDataStorage.getTokens().access?.jwtEncoded ?: ""
        )
    }

    private fun getInitialAuthentificationState(): AuthState {
        val tokens = tokenDataStorage.getTokens()
        val accessToken = tokens.access
        val refreshToken = tokens.refresh
        return if (refreshToken.isExpired()) {
            AuthState.Deauthentificated
        } else {
            AuthState.Authentificated(
                accessToken?.jwtEncoded ?: "",
                refreshToken?.jwtEncoded ?: "",
                tokenDataStorage.getAnonymousState()
            )
        }
    }

    fun getTokens() = tokenDataStorage.getTokens()

    suspend fun registerWithUsernameAndPassword(
        username: String,
        password: String,
        isAnonymous: Boolean
    ): Result<RegisterResponse, CodedThrowable> {
        val registerCredentials =
            RegisterDataSet(username = username, password = password, anonymous = isAnonymous)
        return suspendApiCall {
            queriesService.registerWithUsernameAndPassword(registerCredentials)
        }.onSuccess {
            setAuthState(AuthState.Authentificated(it.accessToken, it.refreshToken, isAnonymous))
        }
    }

    suspend fun deanonymizeUser(
        oldUsername: String,
        oldPassword: String,
        newUsername: String,
        newPassword: String
    ): Result<DeanonimizeResponse, CodedThrowable> {
        val deanonimizeDataSet = DeanonimizeDataSet(
            oldUsername,
            oldPassword,
            newUsername,
            newPassword
        )
        return suspendApiCall { queriesService.deanonymize(deanonimizeDataSet) }
    }

    suspend fun loginWithUsernameAndPassword(
        username: String,
        password: String
    ): Result<LoginResponse, CodedThrowable> {
        val loginDataSet = LoginDataSet(username, password)
        return suspendApiCall {
            queriesService.loginWithUsernameAndPassword(loginDataSet)
        }.onSuccess {
            setAuthState(AuthState.Authentificated(it.accessToken, it.refreshToken))
        }
    }

    suspend fun requestPhoneCode(phoneNumber: String): Result<RequestPhoneCodeResponse, CodedThrowable> {
        val requestPhoneCodeDataSet = RequestPhoneCodeDataSet(phoneNumber)
        return suspendApiCall { queriesService.requestPhoneCode(requestPhoneCodeDataSet) }
    }

    suspend fun phoneLogin(
        phoneLogin: String,
        code: String
    ): Result<PhoneLoginResponse, CodedThrowable> {
        val loginDataSet = PhoneLoginDataSet(phoneLogin, code)
        return suspendApiCall { queriesService.phoneLogin(loginDataSet) }.onSuccess {
            setAuthState(AuthState.Authentificated(it.accessToken, it.refreshToken))
        }
    }

    suspend fun federatedLogin(
        provider: String,
        token: String
    ): Result<FederatedLoginResponse, CodedThrowable> {
        val federatedLoginDataSet = FederatedLoginDataSet(provider, token)
        return suspendApiCall { queriesService.federatedLogin(federatedLoginDataSet) }.onSuccess {
            setAuthState(AuthState.Authentificated(it.accessToken, it.refreshToken))
        }
    }

    suspend fun logout(): Result<Unit, CodedThrowable> {
        return suspendApiCall { queriesService.logout() }.onSuccess {
            setAuthState(AuthState.Deauthentificated)
        }
    }

    suspend fun refreshTokens(): Result<RefreshTokenResponse, CodedThrowable> {
        return suspendApiCall { refreshTokenService.refreshToken() }.onSuccess {
            setAuthState(AuthState.Authentificated(it.accessToken, it.refreshToken, tokenDataStorage.getAnonymousState()))
        }.onError {
            setAuthState(AuthState.Deauthentificated)
        }
    }
}
