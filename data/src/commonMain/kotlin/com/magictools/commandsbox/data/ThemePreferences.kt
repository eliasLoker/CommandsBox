package com.magictools.commandsbox.data

import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

@Serializable
enum class ThemePreference(val isDark: Boolean) {
    LIGHT(isDark = false),
    DARK(isDark = true)
}

interface ThemePreferences {

    val selectedTheme: StateFlow<ThemePreference?>

    fun setTheme(theme: ThemePreference)
}
