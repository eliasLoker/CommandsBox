package com.magictools.commandsBox.commonpresentation.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.magictools.commandsBox.commonpresentation.model.ToastState
import com.magictools.commandsBox.commonpresentation.utils.createViewModelSavedStateHandle
import dev.zacsweers.metro.AppScope
import com.magictools.multimvi.event.listen
import com.magictools.multimvi.state.viewState
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AssistedInject
class MainViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
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

    @AssistedFactory
    @ViewModelAssistedFactoryKey(MainViewModel::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): MainViewModel =
            create(createViewModelSavedStateHandle(extras))

        fun create(@Assisted savedStateHandle: SavedStateHandle): MainViewModel
    }
}
