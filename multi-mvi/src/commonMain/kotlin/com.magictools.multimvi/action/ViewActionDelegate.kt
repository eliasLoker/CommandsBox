package com.magictools.multimvi.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun actions() = object : ReadOnlyProperty<ViewModel, ViewActionEmitter> {
    private var provider: ViewActionEmitterImpl? = null

    override fun getValue(
        thisRef: ViewModel,
        property: KProperty<*>
    ): ViewActionEmitter = provider ?: ViewActionEmitterImpl(
        MutableSharedFlow(replay = 1),
        thisRef.viewModelScope
    ).also { provider = it }
}

fun <T> composeActions() = object : ReadOnlyProperty<Any, MutableSharedFlow<T?>> {
    private val sharedFlow = MutableSharedFlow<T?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): MutableSharedFlow<T?> = sharedFlow
}

@OptIn(ExperimentalCoroutinesApi::class)
private class ViewActionEmitterImpl(
    private val flow: MutableSharedFlow<ViewActionBundle>,
    private val scope: CoroutineScope
) : ViewActionEmitter, Flow<ViewActionBundle> by flow.onEach(action = { flow.resetReplayCache() }) {

    override fun emit(vararg actions: ViewAction) {
        scope.launch { flow.emit(ViewActionBundle(actions.toList())) }
    }

    override suspend fun emitNow(vararg actions: ViewAction) = flow.emit(ViewActionBundle(actions.toList()))
}
