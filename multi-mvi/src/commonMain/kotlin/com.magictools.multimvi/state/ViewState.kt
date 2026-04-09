package com.magictools.multimvi.state

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

interface ViewState

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
interface ViewStateHolder<State : ViewState> : StateFlow<State> {

    override var value: State
}
