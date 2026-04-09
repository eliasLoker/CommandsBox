package com.magictools.commandsBox.commonui.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

val AppMainDispatcher: CoroutineDispatcher
    get() = DispatcherProvider.main

object DispatcherProvider {
    var main: CoroutineDispatcher = Dispatchers.Main

    fun initialize(mainDispatcher: CoroutineDispatcher) {
        main = mainDispatcher
    }
}