package com.magictools.commandsBox.commonpresentation.ui.main

import com.magictools.commandsBox.commonpresentation.model.ToastState
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@Inject
@SingleIn(AppScope::class)
class MainToastEvents {

    private val _event = MutableSharedFlow<ToastState.Data>()
    private val scope = CoroutineScope(Dispatchers.Default)

    fun emit(state: ToastState.Data) {
        scope.launch {
            delay(500)
            _event.emit(state)
        }
    }

    fun observe() = _event.asSharedFlow()
}
