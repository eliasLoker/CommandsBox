package com.magictools.commandsBox.commonpresentation.ui.main

import com.magictools.commandsBox.commonpresentation.model.ToastState
import com.magictools.multimvi.state.ViewState
import kotlinx.serialization.Serializable

@Serializable
data class MainViewState(
    val toastState: ToastState = ToastState()
): ViewState
