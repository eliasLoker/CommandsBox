package com.magictools.commandsBox.commonpresentation.model

import kotlinx.serialization.Serializable

@Serializable
data class CommandState(
    val id: Long,
    val isEditMode: Boolean = false,
    val command: String = "",
    val description: String = "",
    val commandInputViewState: InputViewState = InputViewState(),
    val descriptionInputViewState: InputViewState = InputViewState(),
)
