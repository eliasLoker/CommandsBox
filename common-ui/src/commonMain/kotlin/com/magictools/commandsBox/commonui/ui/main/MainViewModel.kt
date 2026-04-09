package com.magictools.commandsBox.commonui.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magictools.commandsBox.commonui.customview.ToastState
import com.magictools.multimvi.event.listen
import com.magictools.multimvi.state.viewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    mainToastEvents: MainToastEvents
) : ViewModel() {

    val viewState by savedStateHandle.viewState(MainViewState(), MainViewState.serializer())

    val onNewEvent by listen<MainEvent> { event ->
        when (event) {
            OnToastDismissed -> viewState.value = viewState.value.copy(
                toastState = viewState.value.toastState.copy(
                    isVisible = false
                )
            )
        }
    }

    init {
        mainToastEvents.observe().onEach {
            viewState.value = viewState.value.copy(
                toastState = ToastState(
                    data = it,
                    isVisible = true
                )
            )
        }.launchIn(viewModelScope)
    }
}