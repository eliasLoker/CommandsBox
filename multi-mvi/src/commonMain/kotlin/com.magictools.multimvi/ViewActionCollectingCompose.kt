package com.magictools.multimvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.magictools.multimvi.action.DefaultViewAction
import com.magictools.multimvi.action.ViewAction
import com.magictools.multimvi.action.ViewActionBundle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
inline fun <reified Action : ViewAction> Flow<ViewActionBundle>.collectActions(
    crossinline dismiss: () -> Unit = {},
    crossinline handle: suspend (Action) -> Unit,
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        collect { bundle ->
            bundle.forEach { action ->
                when (action) {
                    is DefaultViewAction -> when (action) {
                        DefaultViewAction.Dismiss -> scope.launch { dismiss() }
                    }
                    is Action -> scope.launch { handle(action) }
                    else -> Unit
                }
            }
        }
    }
}
