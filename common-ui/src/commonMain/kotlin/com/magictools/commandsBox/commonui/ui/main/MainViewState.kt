package com.magictools.commandsBox.commonui.ui.main

import com.magictools.commandsBox.commonui.customview.ToastState
import com.magictools.multimvi.state.ViewState
import kotlinx.serialization.Serializable

@Serializable
data class MainViewState(
    val toastState: ToastState = ToastState()
): ViewState
