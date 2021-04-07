package com.madappgang.identifolib.di

import com.madappgang.identifolib.extensions.createWebService
import com.madappgang.identifolib.network.QueriesService
import com.madappgang.identifolib.network.RefreshSessionQueries
import com.madappgang.identifolib.network.interceptors.IdentifoAuthInterceptor
import com.madappgang.identifolib.network.interceptors.IdentifoRefreshInterceptor
import com.madappgang.identifolib.storages.ITokenDataStorage
import com.madappgang.identifolib.storages.IUserStorage
import com.madappgang.identifolib.storages.TokenDataStorage
import com.madappgang.identifolib.storages.UserStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


/*
 * Created by Eugene Prytula on 2/23/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

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

    factory<IdentifoAuthInterceptor> {
        IdentifoAuthInterceptor(
            appId,
            appSecret,
            get<ITokenDataStorage>()
        )
    }

    factory<IdentifoRefreshInterceptor> {
        IdentifoRefreshInterceptor(
            appId,
            appSecret,
            get<ITokenDataStorage>()
        )
    }

    single<CoroutineDispatcher> {
        Dispatchers.IO
    }

    single<QueriesService> {
        OkHttpClient.Builder()
            .addInterceptor(get<IdentifoAuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
            .createWebService(baseUrl)
    }

    single<RefreshSessionQueries> {
        OkHttpClient.Builder()
            .addInterceptor(get<IdentifoRefreshInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
            .createWebService(baseUrl)
    }
}