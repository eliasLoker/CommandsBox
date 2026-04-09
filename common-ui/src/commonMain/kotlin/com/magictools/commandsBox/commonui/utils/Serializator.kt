package com.magictools.commandsBox.commonui.utils

import kotlinx.serialization.json.Json

class Serializator(
    val json: Json
) {
    inline fun <reified T> toJson(obj: T): String =
        json.encodeToString(obj)

    inline fun <reified T> fromJson(jsonString: String): T =
        json.decodeFromString(jsonString)
}