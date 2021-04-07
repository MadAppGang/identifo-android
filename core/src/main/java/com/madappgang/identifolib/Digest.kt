package com.madappgang.identifolib

import android.util.Base64
import okhttp3.RequestBody
import okio.Buffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/*
 * Created by Eugene Prytula on 2/5/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class Digest(private val appSecret: String) {
    private val algorithm = "HmacSHA256"
    private val sha256HMAC = Mac.getInstance(algorithm).apply {
        init(SecretKeySpec(appSecret.toByteArray(), algorithm))
    }

    fun generateDigest(input: String): String {
        sha256HMAC.update(input.toByteArray())
        val digest = Base64.encodeToString(sha256HMAC.doFinal(), Base64.NO_WRAP)
        return "SHA-256=${digest!!}"
    }
}

internal fun RequestBody?.getPreparedDigest(appSecret : String): String {
    val buffer = Buffer().apply { this@getPreparedDigest?.writeTo(this) }
    return Digest(appSecret).generateDigest(buffer.readUtf8())
}