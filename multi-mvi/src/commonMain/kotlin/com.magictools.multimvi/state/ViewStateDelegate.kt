package com.magictools.multimvi.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <State : ViewState> SavedStateHandle.viewState(
    initValue: State,
    serializer: KSerializer<State>
): ReadOnlyProperty<ViewModel, ViewStateHolder<State>> =
    object : ReadOnlyProperty<ViewModel, ViewStateHolder<State>> {

        private val json = Json { ignoreUnknownKeys = true }

        private val stateFlow: MutableStateFlow<State> by lazy {
            val restored = get<String>(SAVED_STATE_HANDLE_KEY)?.let {
                runCatching { json.decodeFromString(serializer, it) }.getOrNull()
            }
            MutableStateFlow(restored ?: initValue)
        }

        private val holder = ViewStateHolderImpl(stateFlow, this@viewState, json, serializer)

        override fun getValue(thisRef: ViewModel, property: KProperty<*>): ViewStateHolder<State> = holder
    }

private const val SAVED_STATE_HANDLE_KEY = "SAVED_STATE_HANDLE_KEY"

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
private class ViewStateHolderImpl<State : ViewState>(
    private val _stateFlow: MutableStateFlow<State>,
    private val savedStateHandle: SavedStateHandle,
    private val json: Json,
    private val serializer: KSerializer<State>
) : ViewStateHolder<State>, StateFlow<State> by _stateFlow {

    override var value: State
        get() = _stateFlow.value
        set(value) {
            _stateFlow.value = value
            savedStateHandle[SAVED_STATE_HANDLE_KEY] = json.encodeToString(serializer, value)
        }
}
