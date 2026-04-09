package com.magictools.commandsBox.commonui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun Modifier.onDesktopScroll(onScroll: (Float) -> Unit): Modifier {
    return this
}