package com.prytula.identifolib.network

import com.prytula.identifolib.entities.deanonymize.DeanonimizeDataSet
import com.prytula.identifolib.entities.deanonymize.DeanonimizeResponse
import com.prytula.identifolib.entities.federatedLogin.FederatedLoginDataSet
import com.prytula.identifolib.entities.federatedLogin.FederatedLoginResponse
import com.prytula.identifolib.entities.logging.LoginDataSet
import com.prytula.identifolib.entities.logging.LoginResponse
import com.prytula.identifolib.entities.logout.LogoutDataSet
import com.prytula.identifolib.entities.phoneLogin.PhoneLoginDataSet
import com.prytula.identifolib.entities.phoneLogin.PhoneLoginResponse
import com.prytula.identifolib.entities.register.RegisterDataSet
import com.prytula.identifolib.entities.register.RegisterResponse
import com.prytula.identifolib.entities.requestCode.RequestPhoneCodeDataSet
import com.prytula.identifolib.entities.requestCode.RequestPhoneCodeResponse
import com.prytula.identifolib.entities.reserPassword.ResetPasswordDataSet
import com.prytula.identifolib.entities.reserPassword.ResetPasswordResponse
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