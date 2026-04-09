package com.magictools.commandsBox.commonui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun CommandsBoxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val appColors = when {
        darkTheme -> darkColors
        else -> lightColors
    }

    val customStyles = AppTextStyle(
        headline = AppTextTokens.headlineLarge,
        body = AppTextTokens.bodyMedium,
        caption = AppTextTokens.labelSmall
    )

    CompositionLocalProvider(
        LocalAppTypography provides customStyles,
        LocalAppColors provides appColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTextTokens,
        ) {
            Surface(
                color = appColors.backgroundMain,
                contentColor = appColors.mainText,
                content = content
            )
        }
    }
}