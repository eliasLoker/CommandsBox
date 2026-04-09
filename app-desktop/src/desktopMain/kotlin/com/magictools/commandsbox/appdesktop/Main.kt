package com.magictools.commandsbox.appdesktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.magictools.commandsBox.commonui.customview.CommandsBoxToastView
import com.magictools.commandsBox.commonui.navigation.MainNavHost
import com.magictools.commandsBox.commonui.theme.CommandsBoxTheme
import com.magictools.commandsBox.commonui.ui.main.MainViewModel
import com.magictools.commandsBox.commonui.ui.main.OnToastDismissed
import com.magictools.commandsBox.commonui.utils.DispatcherProvider
import com.magictools.commandsbox.resources.Res
import com.magictools.commandsbox.resources.app_name
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

fun main() = application {
    DispatcherProvider.initialize(Dispatchers.Default)
    desktopModule()
    Window(
        title = stringResource(Res.string.app_name),
        onCloseRequest = ::exitApplication
    ) {
        val viewModel: MainViewModel = koinViewModel()
        CommandsBoxTheme {
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