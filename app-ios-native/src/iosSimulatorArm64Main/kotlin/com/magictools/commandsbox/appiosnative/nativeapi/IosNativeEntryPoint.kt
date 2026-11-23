package com.magictools.commandsbox.appiosnative.nativeapi

import androidx.lifecycle.viewmodel.CreationExtras
import com.magictools.commandsBox.commonpresentation.ui.home.HomeViewModel
import com.magictools.commandsBox.commonpresentation.ui.main.MainViewModel
import com.magictools.commandsBox.commonpresentation.ui.settings.SettingsViewModel
import com.magictools.commandsBox.commonpresentation.utils.DispatcherProvider
import com.magictools.commandsbox.appiosnative.IosNativeGraph
import dev.zacsweers.metro.createGraph
import kotlinx.coroutines.Dispatchers

private var iosNativeGraph: IosNativeGraph? = null

fun startIosNative() {
    DispatcherProvider.initialize(Dispatchers.Main)
    if (iosNativeGraph == null) {
        iosNativeGraph = createGraph<IosNativeGraph>()
    }
}

fun createIosViewModelHost(): NativeViewModelHost {
    val graph = checkNotNull(iosNativeGraph) {
        "IosNativeGraph is not initialized. Call startIosNative() first."
    }
    val factory = graph.metroViewModelFactory
    return NativeViewModelHost(
        main = MainViewModelBridge(factory.create(MainViewModel::class, CreationExtras.Empty)),
        home = HomeViewModelBridge(factory.create(HomeViewModel::class, CreationExtras.Empty)),
        settings = SettingsViewModelBridge(factory.create(SettingsViewModel::class, CreationExtras.Empty))
    )
}
