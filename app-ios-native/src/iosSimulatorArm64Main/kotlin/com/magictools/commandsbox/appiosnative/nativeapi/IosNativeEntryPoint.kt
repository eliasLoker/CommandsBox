package com.magictools.commandsbox.appiosnative.nativeapi

import androidx.lifecycle.SavedStateHandle
import com.magictools.commandsBox.commonpresentation.commonPresentationModule
import com.magictools.commandsBox.commonpresentation.ui.home.HomeViewModel
import com.magictools.commandsBox.commonpresentation.ui.main.MainViewModel
import com.magictools.commandsBox.commonpresentation.ui.settings.SettingsViewModel
import com.magictools.commandsBox.commonpresentation.utils.CustomClipboardManager
import com.magictools.commandsBox.commonpresentation.utils.DispatcherProvider
import com.magictools.commandsbox.appiosnative.IosNativeClipboardManager
import com.magictools.commandsbox.data.iosDataModule
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools

private val iosNativePlatformModule = module {
    factory { SavedStateHandle() }
    singleOf(::IosNativeClipboardManager).binds(arrayOf(CustomClipboardManager::class))
}

fun startIosNativeKoin() {
    DispatcherProvider.initialize(Dispatchers.Main)
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        startKoin {
            modules(
                iosNativePlatformModule,
                commonPresentationModule,
                iosDataModule
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
