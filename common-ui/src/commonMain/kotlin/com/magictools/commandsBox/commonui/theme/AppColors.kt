package com.magictools.commandsBox.commonui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val backgroundMain: Color,
    val backgroundSecondary: Color,
    val mainText: Color,
    val secondaryText: Color,
    val buttonBackground: Color,
    val buttonText: Color,
    val icon: Color,
    val toastSuccess: Color,
    val toastFailure: Color
)

val lightColors = AppColors(
    backgroundMain = Color(0xFFEAEAEA),
    backgroundSecondary = Color(0xFFFFFFFF),
    mainText = Color(0xFF000000),
    secondaryText = Color(0xFF454545),
    buttonBackground = Color(0xFF000000),
    buttonText = Color(0xFFFFFFFF),
    icon = Color(0xFF000000),
    toastSuccess = Color(0xFF6EFD90),
    toastFailure = Color(0xFFFD7474),
)

val darkColors = AppColors(
    backgroundMain = Color(0xFF191A1C),
    backgroundSecondary = Color(0xFF1E1F22),
    mainText = Color(0xFFBABABA),
    secondaryText = Color(0xFFA8A8A8),
    buttonBackground = Color(0xFFBABABA),
    buttonText = Color(0xFF000000),
    icon = Color(0xFFBABABA),
    toastSuccess = Color(0xFF246319),
    toastFailure = Color(0xFF631919),
)

val LocalAppColors = staticCompositionLocalOf { lightColors }

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}