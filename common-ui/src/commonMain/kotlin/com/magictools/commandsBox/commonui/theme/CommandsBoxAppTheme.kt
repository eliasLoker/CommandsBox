package com.magictools.commandsBox.commonui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.magictools.commandsbox.data.ThemePreferences

@Composable
fun CommandsBoxAppTheme(
    themePreferences: ThemePreferences,
    content: @Composable () -> Unit
) {
    val selectedTheme by themePreferences.selectedTheme.collectAsState()

    CommandsBoxTheme(
        darkTheme = resolveDarkTheme(
            selectedTheme = selectedTheme,
            systemInDarkTheme = isSystemInDarkTheme()
        ),
        content = content
    )
}
