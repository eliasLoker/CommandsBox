package com.magictools.commandsBox.commonui.customview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.magictools.commandsBox.commonui.theme.AppTextTokens
import com.magictools.commandsBox.commonui.theme.AppTheme
import com.magictools.commandsbox.resources.Res
import com.magictools.commandsbox.resources.ic_clear
import com.magictools.commandsbox.resources.ic_command
import com.magictools.commandsbox.resources.ic_details
import com.magictools.commandsbox.resources.ic_tab
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Serializable
data class InputViewState(
    val query: String = "",
    val hint: String = "",
    val maxLines: Int = 1,
    val singleLine: Boolean = true,
    val maxLength: Int = 0,
    val icon: InputIcon = InputIcon.COMMAND
) {
    enum class InputIcon {
        TAB,
        COMMAND,
        DESCRIPTION
    }

}

@Composable
fun InputView(
    modifier: Modifier,
    state: InputViewState,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit = { onQueryChange("") }
) {
    val query = if (state.singleLine) state.query.replace("\n", " ") else state.query
    val focusManager = LocalFocusManager.current
    val backgroundColor = AppTheme.colors.backgroundMain
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                ).padding(
                    horizontal = 8.dp
                ).padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier, verticalAlignment = Alignment.CenterVertically
            ) {
                val icon = when (state.icon) {
                    InputViewState.InputIcon.TAB -> Res.drawable.ic_tab
                    InputViewState.InputIcon.COMMAND -> Res.drawable.ic_command
                    InputViewState.InputIcon.DESCRIPTION -> Res.drawable.ic_details
                }
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(icon),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = state.hint,
                            style = AppTextTokens.bodyMedium.copy(
                                color = AppTheme.colors.icon.copy(
                                    alpha = 0.3f
                                )
                            )
                        )
                    }
                    BasicTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = state.singleLine,
                        maxLines = state.maxLines,
                        cursorBrush = SolidColor(AppTheme.colors.mainText),
                        textStyle = AppTextTokens.bodyMedium.copy(color = AppTheme.colors.mainText),
                        visualTransformation = VisualTransformation.None
                    )
                }
                IconButton(
                    modifier = Modifier.alpha(if (query.isNotEmpty()) 1f else 0f).size(36.dp),
                    onClick = {
                        onClearClick()
                        focusManager.clearFocus()
                    },
                    enabled = query.isNotEmpty()
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = vectorResource(Res.drawable.ic_clear),
                        contentDescription = null,
                        tint = AppTheme.colors.icon
                    )
                }
            }
        }
    }
}

@Composable
@Preview(widthDp = 360, heightDp = 800)
private fun InputViewPreview() {

}