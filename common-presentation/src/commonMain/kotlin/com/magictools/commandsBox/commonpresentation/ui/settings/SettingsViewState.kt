package com.magictools.commandsBox.commonpresentation.ui.settings

import com.magictools.commandsbox.data.ThemePreference
import com.magictools.multimvi.state.ViewState
import kotlinx.serialization.Serializable

@Serializable
data class SettingsViewState(
    val isLoading: Boolean = false,
    val selectedTheme: ThemePreference? = null
) : ViewState
