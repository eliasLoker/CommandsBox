package com.magictools.commandsbox.appiosnative

import com.magictools.commandsBox.commonpresentation.model.ToastState
import com.magictools.commandsBox.commonpresentation.ui.main.MainViewState
import com.magictools.commandsBox.commonpresentation.ui.settings.SettingsAction
import com.magictools.commandsBox.commonpresentation.ui.settings.SettingsViewState

fun isDarkTheme(
    settingsState: SettingsViewState,
    systemInDarkTheme: Boolean,
): Boolean = settingsState.selectedTheme?.isDark ?: systemInDarkTheme

fun isErrorToast(state: MainViewState): Boolean =
    state.toastState.data.type == ToastState.Type.ERROR

fun isNavigateBackAction(action: SettingsAction): Boolean =
    action == SettingsAction.NavigateBack
