package com.magictools.commandsBox.commonui.theme

import com.magictools.commandsbox.data.ThemePreference

internal fun resolveDarkTheme(
    selectedTheme: ThemePreference?,
    systemInDarkTheme: Boolean
): Boolean = selectedTheme?.isDark ?: systemInDarkTheme
