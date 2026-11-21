package com.magictools.commandsbox.appiosnative.nativeapi

import com.magictools.commandsBox.commonpresentation.ui.home.HomeAction
import com.magictools.commandsBox.commonpresentation.ui.home.HomeEvent
import com.magictools.commandsBox.commonpresentation.ui.home.HomeViewModel
import com.magictools.commandsBox.commonpresentation.ui.home.HomeViewState
import com.magictools.commandsBox.commonpresentation.ui.main.MainEvent
import com.magictools.commandsBox.commonpresentation.ui.main.MainViewModel
import com.magictools.commandsBox.commonpresentation.ui.main.MainViewState
import com.magictools.commandsBox.commonpresentation.ui.settings.SettingsAction
import com.magictools.commandsBox.commonpresentation.ui.settings.SettingsEvent
import com.magictools.commandsBox.commonpresentation.ui.settings.SettingsViewModel
import com.magictools.commandsBox.commonpresentation.ui.settings.SettingsViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewModelSubscription internal constructor(
    private val job: Job
) {

    fun cancel() {
        job.cancel()
    }
}

class MainViewModelBridge internal constructor(
    private val viewModel: MainViewModel
) : AutoCloseable {

    private val observations = NativeObservationScope(viewModel.viewState)

    val currentState: MainViewState
        get() = observations.currentState

    fun observeState(onState: (MainViewState) -> Unit): ViewModelSubscription =
        observations.observeState(onState)

    fun dispatch(event: MainEvent) {
        viewModel.onNewEvent(event)
    }

    override fun close() {
        observations.close()
    }
}

class HomeViewModelBridge internal constructor(
    private val viewModel: HomeViewModel
) : AutoCloseable {

    private val observations = NativeObservationScope(viewModel.viewState)

    val currentState: HomeViewState
        get() = observations.currentState

    fun observeState(onState: (HomeViewState) -> Unit): ViewModelSubscription =
        observations.observeState(onState)

    fun observeActions(onAction: (HomeAction) -> Unit): ViewModelSubscription =
        observations.observe(viewModel.action) { bundle ->
            bundle.forEach { action ->
                if (action is HomeAction) {
                    onAction(action)
                }
            }
        }

    fun dispatch(event: HomeEvent) {
        viewModel.onNewEvent(event)
    }

    override fun close() {
        observations.close()
    }
}

class SettingsViewModelBridge internal constructor(
    private val viewModel: SettingsViewModel
) : AutoCloseable {

    private val observations = NativeObservationScope(viewModel.viewState)

    val currentState: SettingsViewState
        get() = observations.currentState

    fun observeState(onState: (SettingsViewState) -> Unit): ViewModelSubscription =
        observations.observeState(onState)

    fun observeActions(onAction: (SettingsAction) -> Unit): ViewModelSubscription =
        observations.observe(viewModel.action) { bundle ->
            bundle.forEach { action ->
                if (action is SettingsAction) {
                    onAction(action)
                }
            }
        }

    fun dispatch(event: SettingsEvent) {
        viewModel.onNewEvent(event)
    }

    override fun close() {
        observations.close()
    }
}

class NativeViewModelHost internal constructor(
    val main: MainViewModelBridge,
    val home: HomeViewModelBridge,
    val settings: SettingsViewModelBridge
) : AutoCloseable {

    override fun close() {
        main.close()
        home.close()
        settings.close()
    }
}

/**
 * Owns everything a bridge needs to observe one ViewModel: the state source, the
 * main-dispatcher scope, subscription creation and cancellation on close. Bridges
 * only expose their typed delegations on top of it, so the observation and
 * cleanup behavior stays identical across Main, Home and Settings.
 */
private class NativeObservationScope<State>(
    private val stateFlow: StateFlow<State>
) : AutoCloseable {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    val currentState: State
        get() = stateFlow.value

    fun observeState(onState: (State) -> Unit): ViewModelSubscription =
        observe(stateFlow, onState)

    fun <T> observe(flow: Flow<T>, onValue: (T) -> Unit): ViewModelSubscription {
        val job = scope.launch {
            flow.collect { value -> onValue(value) }
        }
        return ViewModelSubscription(job)
    }

    override fun close() {
        scope.cancel()
    }
}
