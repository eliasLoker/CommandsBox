package com.magictools.commandsbox.data.internal

import com.magictools.commandsbox.data.ThemePreference
import com.magictools.commandsbox.data.ThemePreferences
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ThemePreferencesImpl(
    private val settings: Settings
) : ThemePreferences {

    private val _selectedTheme = MutableStateFlow(readTheme())

    override val selectedTheme: StateFlow<ThemePreference?> = _selectedTheme.asStateFlow()

    override fun setTheme(theme: ThemePreference) {
        settings.putString(THEME_KEY, theme.name)
        _selectedTheme.value = theme
    }

    private fun readTheme(): ThemePreference? {
        val storedValue = settings.getStringOrNull(THEME_KEY) ?: return null
        return ThemePreference.entries.firstOrNull { it.name == storedValue }
    }

    private companion object {

        const val THEME_KEY = "selected_theme"
    }
}
