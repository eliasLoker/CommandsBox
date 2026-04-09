package com.magictools.commandsBox.commonui.utils

import kotlin.text.Charsets.UTF_8

object UrlEncoder {
    private const val allowedChars =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~"

    fun encode(input: String): String {
        val sb = StringBuilder()
        input.toByteArray(UTF_8).forEach { byte ->
            val ch = byte.toInt().toChar()
            if (allowedChars.contains(ch)) {
                sb.append(ch)
            } else {
                sb.append("%")
                sb.append((byte.toInt() and 0xFF).toString(16).uppercase().padStart(2, '0'))
            }
        }
        return sb.toString()
    }

    fun decode(input: String): String {
        val bytes = mutableListOf<Byte>()
        var i = 0
        while (i < input.length) {
            if (input[i] == '%' && i + 2 < input.length) {
                val hex = input.substring(i + 1, i + 3)
                bytes.add(hex.toInt(16).toByte())
                i += 3
            } else {
                bytes.add(input[i].code.toByte())
                i++
            }
        }
        return bytes.toByteArray().toString(UTF_8)
    }
}
