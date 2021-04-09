package com.madappgang.identifolib.network.interceptors

import com.madappgang.IdentifoAuthentication
import com.madappgang.identifolib.entities.Token
import com.madappgang.identifolib.entities.Tokens
import com.madappgang.identifolib.getPreparedDigest
import com.madappgang.identifolib.network.RefreshSessionQueries
import com.madappgang.identifolib.storages.ITokenDataStorage
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.HttpURLConnection


/*
 * Created by Eugene Prytula on 4/9/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */


class IdentifoRefreshAuthenticator(
    private val tokenDataStorage: ITokenDataStorage,
    private val refreshSessionQueries: RefreshSessionQueries,
    private val appSecret: String,
    private val appID: String
) : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? = authenticateRequestRefreshToken(response.request)

    @Synchronized
    private fun authenticateRequestRefreshToken(initialRequest: Request): Request? {
        return try {
            val responseModel = refreshSessionQueries.refreshToken().execute()

            return when (responseModel.code()) {
                HttpURLConnection.HTTP_OK -> {
                    val responseBody = responseModel.body()
                    val newAccessToken = responseBody?.accessToken!!
                    val newRefreshToken = responseBody?.refreshToken!!
                    tokenDataStorage.setTokens(
                        Tokens(
                            Token.Access(newAccessToken),
                            Token.Refresh(newRefreshToken)
                        )
                    )
                    overrideRequest(initialRequest, newAccessToken)
                }
                else -> {
                    IdentifoAuthentication.clearTokens()
                    null
                }
            }

        } catch (exception: Exception) {
            IdentifoAuthentication.clearTokens()
            null
        }
    }


    private fun overrideRequest(staleRequest: Request, newAccessToken: String): Request {
        return staleRequest.newBuilder()
            .header("X-Identifo-ClientID", appID)
            .header("Authorization", "Bearer $newAccessToken")
            .header("Digest", staleRequest.body.getPreparedDigest(appSecret))
            .build()
    }
}