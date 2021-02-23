package com.prytula.identifolib.di

import com.prytula.identifolib.ITokenDataStorage
import com.prytula.identifolib.IdentifoAuthInterceptor
import com.prytula.identifolib.MockRequestInterceptor
import com.prytula.identifolib.TokenDataStorage
import com.prytula.identifolib.extensions.createWebService
import com.prytula.identifolib.network.QueriesService
import com.prytula.identifolib.network.RefreshSessionQueries
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

val dependenciesModule = module {
    single<ITokenDataStorage> { TokenDataStorage(androidContext()) }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    factory<MockRequestInterceptor> { MockRequestInterceptor(androidContext()) }

    factory<IdentifoAuthInterceptor>(named(IDENTIFO_AUTH_INTERCEPTOR)) {
        IdentifoAuthInterceptor(
            "bk9o707k3t4c72q2qqqq",
            "vUYvSt8rEI7lTPIM96MMwPS3",
            get<ITokenDataStorage>().getTokens().access?.jwtEncoded ?: ""
        )
    }

    factory<IdentifoAuthInterceptor>(named(IDENTIFO_REFRESH_INTERCEPTOR)) {
        IdentifoAuthInterceptor(
            "bk9o707k3t4c72q2qqqq",
            "vUYvSt8rEI7lTPIM96MMwPS3",
            get<ITokenDataStorage>().getTokens().refresh?.jwtEncoded ?: ""
        )
    }

    factory<QueriesService> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<IdentifoAuthInterceptor>(named(IDENTIFO_AUTH_INTERCEPTOR)))
            .addInterceptor(get<MockRequestInterceptor>())
            .build()
            .createWebService("https://identifo.jackrudenko.com")
    }

    factory<RefreshSessionQueries> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<MockRequestInterceptor>())
            .addInterceptor(get<IdentifoAuthInterceptor>(named(IDENTIFO_REFRESH_INTERCEPTOR)))
            .build()
            .createWebService("https://identifo.jackrudenko.com")
    }
}