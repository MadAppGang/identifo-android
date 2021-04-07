package com.madappgang.identifolib.entities


/*
 * Created by Eugene Prytula on 2/5/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

import android.util.Base64
import com.google.gson.JsonObject
import com.google.gson.JsonParser

import org.joda.time.DateTime
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

data class Tokens(val access: Token.Access?, val refresh: Token.Refresh?)

sealed class Token constructor(val jwtEncoded: String) {

    class Access constructor(jwtEncoded: String) : Token(jwtEncoded) {
        init {
            if (type != Type.ACCESS) throw IllegalArgumentException()
        }
    }

    class Refresh constructor(jwtEncoded: String) : Token(jwtEncoded) {
        init {
            if (type != Type.REFRESH) throw IllegalArgumentException()
        }
    }

    /**
     * Who creates this token, usually the domain of your Identifo instance
     */
    val iss: String

    /**
     * UserID, application specific string representation. The subject of token, identifies the user
     */
    val sub: String

    /**
     * Expire date Unix time format
     */
    val exp: DateTime

    /**
     * Issue date Unix time format
     */
    val iat: DateTime

    enum class Type {
        ACCESS,
        REFRESH
    }

    val type: Type

    init {
        val token = decodeToken(jwtEncoded)
        val tokenBody = JsonParser().parse(token["Body"]) as JsonObject

        with(tokenBody) {
            iss = get("iss")?.asString ?: throw IllegalArgumentException("Not found field iss in token")
            sub = get("sub")?.asString ?: throw IllegalArgumentException("Not found field sub in token")
            exp = get("exp")?.asLong?.let { DateTime(it * 1000) } ?:
                    throw IllegalArgumentException("Not found field exp in token")
            iat = get("iat")?.asLong?.let { DateTime(it * 1000) } ?:
                    throw IllegalArgumentException("Not found field iat in token")
            type = with(get("type")?.asString) {
                if (this == null || this == "access") Type.ACCESS else Type.REFRESH
            }
        }
    }

    @Throws(Exception::class)
    private fun decodeToken(JWTEncoded: String): Map<String, String> {
        return try {
            val split =
                    JWTEncoded.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            mapOf(
                    "Header" to getJson(split[0]),
                    "Body" to getJson(split[1])
            )

        } catch (e: UnsupportedEncodingException) {

            mapOf()
        }

    }

    @Throws(UnsupportedEncodingException::class)
    private fun getJson(strEncoded: String): String {
        val decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes, Charset.forName("UTF-8"))
    }
}

fun Token?.isExpired() = this == null || this.exp.isBeforeNow