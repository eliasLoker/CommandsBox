package com.magictools.commandsbox.appdesktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.magictools.commandsBox.commonpresentation.ui.main.MainViewModel
import com.magictools.commandsBox.commonpresentation.ui.main.OnToastDismissed
import com.magictools.commandsBox.commonpresentation.utils.DispatcherProvider
import com.magictools.commandsBox.commonui.customview.CommandsBoxToastView
import com.magictools.commandsBox.commonui.navigation.MainNavHost
import com.magictools.commandsBox.commonui.theme.CommandsBoxAppTheme
import com.magictools.commandsbox.resources.Res
import com.magictools.commandsbox.resources.app_name
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    DispatcherProvider.initialize(Dispatchers.Default)
    val graph = remember { createGraph<DesktopAppGraph>() }
    Window(
        title = stringResource(Res.string.app_name),
        onCloseRequest = ::exitApplication
    ) {
        CompositionLocalProvider(
            LocalMetroViewModelFactory provides graph.metroViewModelFactory
        ) {
            CommandsBoxAppTheme(themePreferences = graph.themePreferences) {
                val viewModel = metroViewModel<MainViewModel>()
                val viewState = viewModel.viewState.collectAsState().value
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
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
