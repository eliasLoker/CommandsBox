package com.magictools.commandsBox.commonui.ui.main

import com.magictools.multimvi.event.ViewEvent

sealed interface MainEvent: ViewEvent

data object OnToastDismissed: MainEvent
