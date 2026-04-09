package com.magictools.commandsBox.commonui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Modifier.onDesktopScroll(onScroll: (Float) -> Unit): Modifier