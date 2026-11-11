package com.magictools.commandsbox.appios

import androidx.lifecycle.SavedStateHandle
import com.magictools.commandsBox.commonui.commonUiModule
import com.magictools.commandsBox.commonui.iosCommonUiModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

private val iosPlatformModule = module {
    factory { SavedStateHandle() }
}

fun startIosKoin() {
    startKoin {
        modules(
            iosPlatformModule,
            commonUiModule,
            iosCommonUiModule
        )
    }
}
