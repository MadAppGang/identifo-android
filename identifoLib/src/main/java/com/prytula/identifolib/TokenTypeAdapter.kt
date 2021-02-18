package com.prytula.identifolib

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.prytula.identifolib.entities.Token
import com.prytula.identifolib.entities.Tokens
import java.lang.reflect.Type


/*
 * Created by Eugene Prytula on 2/5/21.
 * Copyright (c) 2021 MadAppGang. All rights reserved.
 */

class TokensTypeAdapter : JsonDeserializer<Tokens> {

    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ): Tokens {

        val accessToken = json.asJsonObject.get("access_token")?.asString?.let { Token.Access(it) }
                ?: throw IllegalArgumentException("access_token not found")

        val refreshToken = json.asJsonObject.get("refresh_token")?.asString?.let { Token.Refresh(it) }
                ?: throw IllegalArgumentException("refresh_token not found")

        return Tokens(accessToken, refreshToken)
    }

}

class AccessTokenTypeAdapter : JsonDeserializer<Token.Access> {

    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ): Token.Access {

        return json.asJsonObject.get("access_token")?.asString?.let { Token.Access(it) }
                ?: throw IllegalArgumentException("access_token not found")

    }
}