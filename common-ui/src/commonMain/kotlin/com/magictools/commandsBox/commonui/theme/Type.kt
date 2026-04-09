package com.magictools.commandsBox.commonui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class AppTextStyle(
    val headline: TextStyle,
    val body: TextStyle,
    val caption: TextStyle
)

val LocalAppTypography = staticCompositionLocalOf {
    AppTextStyle(
        headline = TextStyle.Default,
        body = TextStyle.Default,
        caption = TextStyle.Default
    )
}