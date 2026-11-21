package com.magictools.commandsBox.commonpresentation.model

import kotlinx.serialization.Serializable

@Serializable
data class AddBannerViewState(
    val isExpanded: Boolean = false,
    val tabInputViewState: InputViewState = InputViewState(),
    val commandInputViewState: InputViewState = InputViewState(),
    val descriptionInputViewState: InputViewState = InputViewState(),
    val hasTabs: Boolean = false,
    val selectedTabName: String = ""
)
