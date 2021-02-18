package com.prytula.workManagers

import android.content.Context
import androidx.work.*
import com.prytula.IdentifoAuth
import com.prytula.identifolib.extensions.isSuccessful
import com.prytula.identifolib.extensions.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit


/*
 * Created by Eugene Prytula on 2/16/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class RefreshTokenWorkManager(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val REFRESH_TOKEN_TAG = "refresh_token_tag"
        private val RESERVE_TIME = TimeUnit.MINUTES.toMillis(20)

        fun startWorker(context: Context) {
            IdentifoAuth.getAccessToken()?.exp?.let {
                val currentDate = Calendar.getInstance()

                val timeDiff = (it.millis - RESERVE_TIME) - currentDate.timeInMillis

                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val dailyWorkRequest = OneTimeWorkRequestBuilder<RefreshTokenWorkManager>()
                    .setConstraints(constraints)
                    .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                    .addTag(REFRESH_TOKEN_TAG)
                    .build()
                WorkManager.getInstance(context).enqueue(dailyWorkRequest)
            }
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(REFRESH_TOKEN_TAG)
        }
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val result = IdentifoAuth.refreshTokens().onSuccess { startWorker(context = applicationContext) }
            if (result.isSuccessful()) {
                return@withContext Result.success()
            } else {
                return@withContext Result.retry()
            }
        }
    }
}