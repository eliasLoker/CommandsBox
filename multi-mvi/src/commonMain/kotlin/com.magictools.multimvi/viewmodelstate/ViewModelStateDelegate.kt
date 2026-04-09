package com.magictools.multimvi.viewmodelstate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified State : Any> SavedStateHandle.state(
    key: String = "SAVED_STATE_HANDLE_KEY",
    initialValue: State
): ReadWriteProperty<ViewModel, State> {

    return object : ReadWriteProperty<ViewModel, State> {


        private val json = Json { ignoreUnknownKeys = true }

        private val stateFlow: MutableStateFlow<State> = run {
            val restoredJson = get<String>(key)
            val restored = restoredJson?.let {
                runCatching { json.decodeFromString<State>(it) }.getOrNull()
            }
            MutableStateFlow(restored ?: initialValue)
        }

        private var isCollecting = false

        override fun getValue(thisRef: ViewModel, property: KProperty<*>): State {

            if (!isCollecting) {
                thisRef.viewModelScope.launch {
                    stateFlow.collect { newValue ->
                        set(key, json.encodeToString(newValue))
                    }
                }
                isCollecting = true
            }
            return stateFlow.value
        }

        override fun setValue(thisRef: ViewModel, property: KProperty<*>, value: State) {
            stateFlow.value = value
        }
    }
}