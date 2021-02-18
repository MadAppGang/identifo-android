package com.prytula.identifolib.network.queries

import com.prytula.identifolib.BuildConfig
import com.prytula.identifolib.MockRequestInterceptor.Companion.MOCK
import com.prytula.identifolib.entities.deanonymize.DeanonimizeDataSet
import com.prytula.identifolib.entities.deanonymize.DeanonimizeResponse
import com.prytula.identifolib.entities.federatedLogin.FederatedLoginDataSet
import com.prytula.identifolib.entities.federatedLogin.FederatedLoginResponse
import com.prytula.identifolib.entities.logging.LoginDataSet
import com.prytula.identifolib.entities.logging.LoginResponse
import com.prytula.identifolib.entities.phoneLogin.PhoneLoginDataSet
import com.prytula.identifolib.entities.phoneLogin.PhoneLoginResponse
import com.prytula.identifolib.entities.register.RegisterDataSet
import com.prytula.identifolib.entities.register.RegisterResponse
import com.prytula.identifolib.entities.requestCode.RequestPhoneCodeDataSet
import com.prytula.identifolib.entities.requestCode.RequestPhoneCodeResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

interface QueriesService {
    @Headers("${MOCK}:${BuildConfig.MOCK_FLAG}")
    @POST("/auth/register")
    suspend fun registerWithUsernameAndPassword(@Body registerDataSet: RegisterDataSet): RegisterResponse

    @Headers("${MOCK}:${BuildConfig.MOCK_FLAG}")
    @POST("/me")
    suspend fun deanonymize(@Body deanonimizeDataSet: DeanonimizeDataSet): DeanonimizeResponse

    @Headers("${MOCK}:${BuildConfig.MOCK_FLAG}")
    @POST("/auth/login")
    suspend fun loginWithUsernameAndPassword(@Body loginDataSet: LoginDataSet): LoginResponse

    @Headers("${MOCK}:${BuildConfig.MOCK_FLAG}")
    @POST("/auth/request_phone_code")
    suspend fun requestPhoneCode(@Body requestPhoneCodeDataSet: RequestPhoneCodeDataSet): RequestPhoneCodeResponse

    @Headers("${MOCK}:${BuildConfig.MOCK_FLAG}")
    @POST("/auth/phone_login")
    suspend fun phoneLogin(@Body phoneLoginDataSet: PhoneLoginDataSet) : PhoneLoginResponse

    @Headers("${MOCK}:${BuildConfig.MOCK_FLAG}")
    @POST("/auth/federated")
    suspend fun federatedLogin(@Body federatedLoginDataSet: FederatedLoginDataSet) : FederatedLoginResponse

    @Headers("${MOCK}:${BuildConfig.MOCK_FLAG}")
    @POST("/me/logout")
    suspend fun logout()
}