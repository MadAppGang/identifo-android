package com.prytula.identifolibui.login.phoneNumber.oneTimePassword


/*
 * Created by Eugene Prytula on 4/5/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

sealed class OneTimePasswordTimerStates {
    data class TimerClick(val timeLeft : Long) : OneTimePasswordTimerStates()
    object PossibleToSendCode : OneTimePasswordTimerStates()
}