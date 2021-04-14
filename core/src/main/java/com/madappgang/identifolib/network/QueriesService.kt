package com.madappgang.identifolib.network

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
import retrofit2.http.Body
import retrofit2.http.POST


/*
 * Created by Eugene Prytula on 2/8/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

interface QueriesService {
    @POST("/auth/register")
    suspend fun registerWithUsernameAndPassword(@Body registerDataSet: RegisterDataSet): RegisterResponse

    @POST("/me")
    suspend fun deanonymize(@Body deanonimizeDataSet: DeanonimizeDataSet): DeanonimizeResponse

    @POST("/auth/login")
    suspend fun loginWithUsernameAndPassword(@Body loginDataSet: LoginDataSet): LoginResponse

    @POST("/auth/request_phone_code")
    suspend fun requestPhoneCode(@Body requestPhoneCodeDataSet: RequestPhoneCodeDataSet): RequestPhoneCodeResponse

    @POST("/auth/reset_password")
    suspend fun resetPassword(@Body resetPasswordDataSet: ResetPasswordDataSet): ResetPasswordResponse

    @POST("/auth/phone_login")
    suspend fun phoneLogin(@Body phoneLoginDataSet: PhoneLoginDataSet): PhoneLoginResponse

    @POST("/auth/federated")
    suspend fun federatedLogin(@Body federatedLoginDataSet: FederatedLoginDataSet): FederatedLoginResponse

    @POST("/me/logout")
    suspend fun logout(@Body logoutDataSet: LogoutDataSet)
}