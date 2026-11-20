package com.magictools.commandsBox.commonui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.magictools.commandsbox.data.ThemePreferences
import org.koin.compose.koinInject

@Composable
fun CommandsBoxAppTheme(
    content: @Composable () -> Unit
) {
    val themePreferences = koinInject<ThemePreferences>()
    val selectedTheme by themePreferences.selectedTheme.collectAsState()

    CommandsBoxTheme(
        darkTheme = resolveDarkTheme(
            selectedTheme = selectedTheme,
            systemInDarkTheme = isSystemInDarkTheme()
        ),
        content = content
    )
}
