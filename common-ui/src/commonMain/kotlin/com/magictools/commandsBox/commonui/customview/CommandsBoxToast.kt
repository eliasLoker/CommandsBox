package com.magictools.commandsBox.commonui.customview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.magictools.commandsBox.commonui.theme.AppTextTokens
import com.magictools.commandsBox.commonui.theme.AppTheme
import com.magictools.commandsBox.commonui.theme.CommandsBoxTheme
import com.magictools.commandsbox.resources.Res
import com.magictools.commandsbox.resources.ic_failure
import com.magictools.commandsbox.resources.ic_success
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource

@Serializable
data class ToastState(
    val data: Data = Data(),
    val isVisible: Boolean = false,
    val durationMs: Long = 3000L
) {

    @Serializable
    data class Data(
        val type: Type = Type.SUCCESS,
        val message: String = "",
    )

    enum class Type {
        SUCCESS,
        ERROR
    }
}

@Composable
fun CommandsBoxToastView(
    modifier: Modifier,
    state: ToastState,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = state.isVisible,
        enter = slideInVertically(initialOffsetY = { -it / 2 }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it / 2 }) + fadeOut(),
        modifier = modifier
            .padding(top = 20.dp, start = 16.dp, end = 16.dp)
            .wrapContentSize()
    ) {
        LaunchedEffect(state.data.message, state.isVisible) {
            if (state.isVisible) {
                delay(state.durationMs)
                onDismiss()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = when (state.data.type) {
                        ToastState.Type.SUCCESS -> AppTheme.colors.toastSuccess
                        ToastState.Type.ERROR -> AppTheme.colors.toastFailure
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = AppTheme.colors.mainText.copy(alpha = 0.2f)
                )
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    when (state.data.type) {
                        ToastState.Type.SUCCESS -> Res.drawable.ic_success
                        ToastState.Type.ERROR -> Res.drawable.ic_failure
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(color = AppTheme.colors.mainText)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.data.message,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                style = AppTextTokens.bodySmall.copy(color = AppTheme.colors.mainText)
            )
        }
    }
}

@Composable
@Preview(widthDp = 360, heightDp = 800)
@Preview(widthDp = 360, heightDp = 800, uiMode = UI_MODE_NIGHT_YES)
private fun CustomToastViewPreview() {
    CommandsBoxTheme {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(color = AppTheme.colors.backgroundMain)
                    .padding(top = 16.dp)
            ) {
                CommandsBoxToastView(
                    modifier = Modifier,
                    state = ToastState(
                        isVisible = true,
                        data = ToastState.Data(
                            type = ToastState.Type.SUCCESS,
                            message = "Toast message"
                        ),
                    ),
                    onDismiss = {}
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(color = AppTheme.colors.backgroundMain)
                    .padding(top = 16.dp)
            ) {
                CommandsBoxToastView(
                    modifier = Modifier,
                    state = ToastState(
                        isVisible = true,
                        data = ToastState.Data(
                            type = ToastState.Type.ERROR,
                            message = "Toast message"
                        ),
                    ),
                    onDismiss = {}
                )
            }
        }
    }
}