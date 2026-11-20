package com.magictools.commandsbox.appiosnative

import com.magictools.commandsBox.commonui.customview.ToastState
import com.magictools.commandsBox.commonui.theme.resolveDarkTheme
import com.magictools.commandsBox.commonui.ui.main.MainViewState
import com.magictools.commandsBox.commonui.ui.settings.SettingsAction
import com.magictools.commandsBox.commonui.ui.settings.SettingsViewState

fun isDarkTheme(
    settingsState: SettingsViewState,
    systemInDarkTheme: Boolean,
): Boolean = resolveDarkTheme(
    selectedTheme = settingsState.selectedTheme,
    systemInDarkTheme = systemInDarkTheme,
)

fun isErrorToast(state: MainViewState): Boolean =
    state.toastState.data.type == ToastState.Type.ERROR

fun isNavigateBackAction(action: SettingsAction): Boolean =
    action == SettingsAction.NavigateBack
