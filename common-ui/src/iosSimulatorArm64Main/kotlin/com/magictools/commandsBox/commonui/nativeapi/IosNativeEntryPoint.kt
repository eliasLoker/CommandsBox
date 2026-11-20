package com.magictools.commandsBox.commonui.nativeapi

import androidx.lifecycle.SavedStateHandle
import com.magictools.commandsBox.commonui.commonUiModule
import com.magictools.commandsBox.commonui.iosCommonUiModule
import com.magictools.commandsBox.commonui.ui.home.HomeViewModel
import com.magictools.commandsBox.commonui.ui.main.MainViewModel
import com.magictools.commandsBox.commonui.ui.settings.SettingsViewModel
import com.magictools.commandsBox.commonui.utils.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools

private val iosPlatformModule = module {
    factory { SavedStateHandle() }
}

fun startIosNativeKoin() {
    DispatcherProvider.initialize(Dispatchers.Main)
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        startKoin {
            modules(
                iosPlatformModule,
                commonUiModule,
                iosCommonUiModule
            )
        }
    }
}

fun createIosViewModelHost(): NativeViewModelHost {
    val koin = KoinPlatformTools.defaultContext().get()
    return NativeViewModelHost(
        main = MainViewModelBridge(koin.get<MainViewModel>()),
        home = HomeViewModelBridge(koin.get<HomeViewModel>()),
        settings = SettingsViewModelBridge(koin.get<SettingsViewModel>())
    )
}
