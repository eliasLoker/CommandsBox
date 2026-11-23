package com.magictools.commandsbox.appios

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.magictools.commandsBox.commonpresentation.ui.main.MainViewModel
import com.magictools.commandsBox.commonpresentation.ui.main.OnToastDismissed
import com.magictools.commandsBox.commonpresentation.utils.DispatcherProvider
import com.magictools.commandsBox.commonui.customview.CommandsBoxToastView
import com.magictools.commandsBox.commonui.navigation.MainNavHost
import com.magictools.commandsBox.commonui.theme.CommandsBoxAppTheme
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.Dispatchers
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    DispatcherProvider.initialize(Dispatchers.Main)
    return ComposeUIViewController {
        val graph = remember { createGraph<IosAppGraph>() }
        CompositionLocalProvider(
            LocalMetroViewModelFactory provides graph.metroViewModelFactory
        ) {
            CommandsBoxAppTheme(themePreferences = graph.themePreferences) {
                val viewModel = metroViewModel<MainViewModel>()
                val viewState = viewModel.viewState.collectAsState().value
                Box(modifier = Modifier.fillMaxSize()) {
                    MainNavHost()
                    CommandsBoxToastView(
                        modifier = Modifier.align(Alignment.TopCenter),
                        state = viewState.toastState,
                        onDismiss = { viewModel.onNewEvent(OnToastDismissed) }
                    )
                }
            }
        }
    }
}
