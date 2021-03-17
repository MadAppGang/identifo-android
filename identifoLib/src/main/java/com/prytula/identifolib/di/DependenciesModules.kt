package com.prytula.identifolib.di

import com.prytula.identifolib.storages.ITokenDataStorage
import com.prytula.identifolib.IdentifoAuthInterceptor
import com.prytula.identifolib.MockRequestInterceptor
import com.prytula.identifolib.storages.TokenDataStorage
import com.prytula.identifolib.extensions.createWebService
import com.prytula.identifolib.network.QueriesService
import com.prytula.identifolib.network.RefreshSessionQueries
import com.prytula.identifolib.storages.IUserStorage
import com.prytula.identifolib.storages.UserStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


/*
 * Created by Eugene Prytula on 2/23/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

private const val IDENTIFO_AUTH_INTERCEPTOR = "indentifo_auth_interceptor"
private const val IDENTIFO_REFRESH_INTERCEPTOR = "identifo_refresh_interceptor"

fun dependenciesModule(
    appId: String,
    appSecret: String,
    baseUrl: String
) = module {
    single<ITokenDataStorage> { TokenDataStorage(androidContext()) }
    single<IUserStorage> { UserStorage(androidContext()) }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    factory<MockRequestInterceptor> { MockRequestInterceptor(androidContext()) }

    factory<IdentifoAuthInterceptor>(named(IDENTIFO_AUTH_INTERCEPTOR)) {
        IdentifoAuthInterceptor(
            appId,
            appSecret,
            get<ITokenDataStorage>().getTokens().access?.jwtEncoded ?: ""
        )
    }

    factory<IdentifoAuthInterceptor>(named(IDENTIFO_REFRESH_INTERCEPTOR)) {
        IdentifoAuthInterceptor(
            appId,
            appSecret,
            get<ITokenDataStorage>().getTokens().refresh?.jwtEncoded ?: ""
        )
    }

    single<CoroutineDispatcher> {
        Dispatchers.IO
    }

    single<QueriesService> {
        OkHttpClient.Builder()
            .addInterceptor(get() as HttpLoggingInterceptor)
            .addInterceptor(get(named(IDENTIFO_AUTH_INTERCEPTOR)) as IdentifoAuthInterceptor)
            .addInterceptor(get() as MockRequestInterceptor)
            .build()
            .createWebService(baseUrl)
    }

    single<RefreshSessionQueries> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<MockRequestInterceptor>())
            .addInterceptor(get(named(IDENTIFO_REFRESH_INTERCEPTOR)) as IdentifoAuthInterceptor)
            .build()
            .createWebService(baseUrl)
    }
}