package com.magictools.commandsBox.commonui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun Modifier.onDesktopScroll(onScroll: (Float) -> Unit): Modifier {
    return this.onPointerEvent(PointerEventType.Scroll) {
        onScroll(it.changes.first().scrollDelta.y)
    }
}