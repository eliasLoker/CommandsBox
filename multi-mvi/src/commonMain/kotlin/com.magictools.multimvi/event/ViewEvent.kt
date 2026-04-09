package com.magictools.multimvi.event

import androidx.lifecycle.ViewModel
import kotlin.properties.ReadOnlyProperty

interface ViewEvent

fun <Event : ViewEvent> listen(callback: (Event) -> Unit) =
    ReadOnlyProperty<ViewModel, (Event) -> Unit> { _, _ -> callback }