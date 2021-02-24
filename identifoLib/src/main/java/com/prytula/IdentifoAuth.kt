package com.prytula

import android.content.Context
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.google.gson.GsonBuilder
import com.prytula.identifolib.AccessTokenTypeAdapter
import com.prytula.identifolib.ITokenDataStorage
import com.prytula.identifolib.TokensTypeAdapter
import com.prytula.identifolib.di.dependenciesModule
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
import com.prytula.identifolib.network.QueriesService
import com.prytula.identifolib.network.RefreshSessionQueries
import kotlinx.coroutines.Dispatchers
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

object IdentifoAuth : KoinComponent {
    private val tokenDataStorage by inject<ITokenDataStorage>()
    private val queriesService by inject<QueriesService>()
    private val refreshTokenService by inject<RefreshSessionQueries>()

    private val _authState by lazy { MutableStateFlow(getInitialAuthentificationState()) }
    val authState by lazy { _authState.asStateFlow() }

    fun initAuthenticator(
        context: Context,
        baseUrl: String,
        appId: String,
        secretKey: String
    ) {
        val appContext = context.applicationContext

        Kotpref.apply {
            init(appContext)
            gson = GsonBuilder()
                .registerTypeAdapter(Tokens::class.java, TokensTypeAdapter())
                .registerTypeAdapter(Token.Access::class.java, AccessTokenTypeAdapter())
                .create()
        }

        startKoin {
            androidContext(appContext)
            modules(
                dependenciesModule(
                    appId = appId,
                    appSecret = secretKey,
                    baseUrl = baseUrl
                )
            )
        }
    }

    private fun getInitialAuthentificationState(): AuthState {
        val tokens = tokenDataStorage.getTokens()
        val refreshToken = tokens.refresh
        return if (refreshToken.isExpired()) {
            AuthState.Deauthentificated
        } else {
            AuthState.Authentificated(tokenDataStorage.getAnonymousState())
        }
    }

    private fun saveTokens(
        accessToken: String,
        refreshToken: String,
        isAnonymous: Boolean = false
    ) {
        tokenDataStorage.setTokens(
            Tokens(
                Token.Access(accessToken),
                Token.Refresh(refreshToken)
            )
        )
        tokenDataStorage.setAnonymousState(isAnonymous)
        _authState.value = AuthState.Authentificated(anonymousState = isAnonymous)
    }

    private fun clearTokens() {
        tokenDataStorage.clearAll()
        _authState.value = AuthState.Deauthentificated
    }

    suspend fun registerWithUsernameAndPassword(
        username: String,
        password: String,
        isAnonymous: Boolean
    ): Result<RegisterResponse, ErrorResponse> = withContext(Dispatchers.IO) {
        val registerCredentials =
            RegisterDataSet(username = username, password = password, anonymous = isAnonymous)
        return@withContext suspendApiCall {
            queriesService.registerWithUsernameAndPassword(registerCredentials)
        }.onSuccess {
            saveTokens(it.accessToken, it.refreshToken, isAnonymous)
        }
    }

    suspend fun deanonymizeUser(
        oldUsername: String,
        oldPassword: String,
        newUsername: String,
        newPassword: String
    ): Result<DeanonimizeResponse, ErrorResponse> = withContext(Dispatchers.IO) {
        val deanonimizeDataSet = DeanonimizeDataSet(
            oldUsername,
            oldPassword,
            newUsername,
            newPassword
        )
        return@withContext suspendApiCall { queriesService.deanonymize(deanonimizeDataSet) }
    }

    suspend fun loginWithUsernameAndPassword(
        username: String,
        password: String
    ): Result<LoginResponse, ErrorResponse> = withContext(Dispatchers.IO) {
        val loginDataSet = LoginDataSet(username, password)
        return@withContext suspendApiCall {
            queriesService.loginWithUsernameAndPassword(loginDataSet)
        }.onSuccess {
            saveTokens(it.accessToken, it.refreshToken)
        }
    }

    suspend fun requestPhoneCode(phoneNumber: String): Result<RequestPhoneCodeResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            val requestPhoneCodeDataSet = RequestPhoneCodeDataSet(phoneNumber)
            return@withContext suspendApiCall {
                queriesService.requestPhoneCode(
                    requestPhoneCodeDataSet
                )
            }
        }

    suspend fun phoneLogin(
        phoneLogin: String,
        code: String
    ): Result<PhoneLoginResponse, ErrorResponse> = withContext(Dispatchers.IO) {
        val loginDataSet = PhoneLoginDataSet(phoneLogin, code)
        return@withContext suspendApiCall { queriesService.phoneLogin(loginDataSet) }.onSuccess {
            saveTokens(it.accessToken, it.refreshToken)
        }
    }

    suspend fun federatedLogin(
        provider: String,
        token: String
    ): Result<FederatedLoginResponse, ErrorResponse> = withContext(Dispatchers.IO) {
        val federatedLoginDataSet = FederatedLoginDataSet(provider, token)
        return@withContext suspendApiCall { queriesService.federatedLogin(federatedLoginDataSet) }.onSuccess {
            saveTokens(it.accessToken, it.refreshToken)
        }
    }

    suspend fun logout(): Result<Unit, ErrorResponse> = withContext(Dispatchers.IO){
        return@withContext suspendApiCall { queriesService.logout() }.onSuccess {
            clearTokens()
        }
    }

    suspend fun refreshTokens(): Result<RefreshTokenResponse, ErrorResponse> = withContext(Dispatchers.IO){
        return@withContext suspendApiCall { refreshTokenService.refreshToken() }.onSuccess {
            saveTokens(it.accessToken, it.refreshToken, tokenDataStorage.getAnonymousState())
        }.onError {
            clearTokens()
        }
    }
}
