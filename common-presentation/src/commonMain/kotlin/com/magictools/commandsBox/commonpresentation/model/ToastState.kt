package com.magictools.commandsBox.commonpresentation.model

import kotlinx.serialization.Serializable

@Serializable
data class ToastState(
    val data: Data = Data(),
    val isVisible: Boolean = false,
    val durationMs: Long = 3000L
) {

    @Serializable
    data class Data(
        val type: Type = Type.SUCCESS,
        val message: String = "",
    )

    enum class Type {
        SUCCESS,
        ERROR
    }
}
