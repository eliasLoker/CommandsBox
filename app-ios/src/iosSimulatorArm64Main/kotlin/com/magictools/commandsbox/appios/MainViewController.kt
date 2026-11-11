package com.magictools.commandsbox.appios

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.magictools.commandsBox.commonui.customview.CommandsBoxToastView
import com.magictools.commandsBox.commonui.navigation.MainNavHost
import com.magictools.commandsBox.commonui.theme.CommandsBoxTheme
import com.magictools.commandsBox.commonui.ui.main.MainViewModel
import com.magictools.commandsBox.commonui.ui.main.OnToastDismissed
import com.magictools.commandsBox.commonui.utils.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import org.koin.compose.viewmodel.koinViewModel
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    DispatcherProvider.initialize(Dispatchers.Main)
    startIosKoin()
    return ComposeUIViewController {
        val viewModel: MainViewModel = koinViewModel()
        CommandsBoxTheme {
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
