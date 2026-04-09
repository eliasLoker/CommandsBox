package com.magictools.commandsbox.appandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.magictools.commandsBox.commonui.customview.CommandsBoxToastView
import com.magictools.commandsBox.commonui.navigation.MainNavHost
import com.magictools.commandsBox.commonui.theme.CommandsBoxTheme
import com.magictools.commandsBox.commonui.ui.main.MainViewModel
import com.magictools.commandsBox.commonui.ui.main.OnToastDismissed
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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
}