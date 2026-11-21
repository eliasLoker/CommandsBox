package com.magictools.commandsBox.commonpresentation.model

import kotlinx.serialization.Serializable

@Serializable
data class InputViewState(
    val query: String = "",
    val hint: String = "",
    val maxLines: Int = 1,
    val singleLine: Boolean = true,
    val maxLength: Int = 0,
    val icon: InputIcon = InputIcon.COMMAND
) {
    enum class InputIcon {
        TAB,
        COMMAND,
        DESCRIPTION
    }
}
