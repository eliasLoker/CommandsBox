package com.magictools.commandsbox.appdesktop

import androidx.lifecycle.SavedStateHandle
import com.magictools.commandsBox.commonui.commonUiModule
import com.magictools.commandsBox.commonui.desktopCommonUiModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun desktopModule() = module {
    startKoin {
        factory { SavedStateHandle() }
        modules(
            commonUiModule,
            desktopCommonUiModule
        )
    }
}