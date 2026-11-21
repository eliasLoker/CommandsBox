package com.magictools.commandsBox.commonpresentation.ui.settings

import com.magictools.multimvi.action.ViewAction
import com.magictools.multimvi.event.ViewEvent

sealed interface SettingsAction : ViewAction {
    data object NavigateBack : SettingsAction
}

sealed interface SettingsEvent : ViewEvent
data object OnBackClicked : SettingsEvent
class OnThemeChanged(val isDark: Boolean) : SettingsEvent
