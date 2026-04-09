package com.magictools.commandsBox.commonui.customview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.magictools.commandsBox.commonui.theme.AppTextTokens
import com.magictools.commandsBox.commonui.theme.AppTheme
import com.magictools.commandsBox.commonui.theme.CommandsBoxTheme
import com.magictools.commandsbox.resources.CascadiaCodeItalic
import com.magictools.commandsbox.resources.Res
import com.magictools.commandsbox.resources.delete
import com.magictools.commandsbox.resources.edit
import com.magictools.commandsbox.resources.ic_arrow_up_with_tail
import com.magictools.commandsbox.resources.ic_clear
import com.magictools.commandsbox.resources.ic_copy
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.resources.Font

@Serializable
data class CommandState(
    val id: Long,
    val isEditMode: Boolean = false,
    val command: String = "",
    val description: String = "",
    val commandInputViewState: InputViewState = InputViewState(),
    val descriptionInputViewState: InputViewState = InputViewState(),
)

@Composable
fun CommandView(
    state: CommandState,
    fontFamily: FontFamily,
    indexInList: Int,
    size: Int,
    onCommandQueryChange: (Long, String) -> Unit,
    onDescriptionQueryChange: (Long, String) -> Unit,
    onCloseEditClicked: (Long) -> Unit,
    onEditApplyClicked: (Long) -> Unit,
    onCopyClicked: (Long) -> Unit,
    onEnableEditClicked: (Long) -> Unit,
    onDeleteClicked: (Long) -> Unit,
    onUpClicked: (Long) -> Unit,
    onDownClicked: (Long) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(
                color = AppTheme.colors.backgroundSecondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        when {
            state.isEditMode -> {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.TopEnd)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { onCloseEditClicked(state.id) }
                            ),
                        imageVector = vectorResource(Res.drawable.ic_clear),
                        contentDescription = null,
                        tint = AppTheme.colors.icon
                    )
                }
                InputView(
                    modifier = Modifier.padding(top = 8.dp),
                    state = state.commandInputViewState,
                    onQueryChange = { onCommandQueryChange(state.id, it) }
                )
                InputView(
                    modifier = Modifier.padding(top = 8.dp),
                    state = state.descriptionInputViewState,
                    onQueryChange = { onDescriptionQueryChange(state.id, it) }
                )
                CommandsBoxButton(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(Res.string.edit),
                    onClick = { onEditApplyClicked(state.id) }
                )
            }

            else -> {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { onCopyClicked(state.id) }
                            ),
                        imageVector = vectorResource(Res.drawable.ic_copy),
                        contentDescription = null,
                        tint = AppTheme.colors.icon
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = state.command,
                            fontFamily = fontFamily,
                            maxLines = 10,
                            overflow = TextOverflow.Ellipsis,
                            style = AppTextTokens.headlineMedium.copy(AppTheme.colors.mainText),
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            text = state.description,
                            maxLines = 10,
                            overflow = TextOverflow.Ellipsis,
                            style = AppTextTokens.bodyMedium.copy(AppTheme.colors.secondaryText)
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 32.dp)
                                .background(
                                    color = AppTheme.colors.backgroundMain,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = { onEnableEditClicked(state.id) }
                                    ),
                                text = stringResource(Res.string.edit).uppercase(),
                                style = AppTextTokens.bodyMedium.copy(
                                    color = AppTheme.colors.secondaryText,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 3.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                            Box(
                                modifier = Modifier.height(12.dp)
                                    .width(1.dp)
                                    .background(color = AppTheme.colors.icon)
                            )
                            Text(
                                modifier = Modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = { onDeleteClicked(state.id) }
                                    )
                                    .weight(1f),
                                text = stringResource(Res.string.delete).uppercase(),
                                style = AppTextTokens.bodyMedium.copy(
                                    color = AppTheme.colors.secondaryText,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 3.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    when {
                        size == 1 -> {
                            Spacer(modifier = Modifier.width(36.dp))
                        }
                        else -> {
                            Column(
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                if (indexInList != 0) {
                                    Icon(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() },
                                                onClick = { onUpClicked(state.id) }
                                            ),
                                        imageVector = vectorResource(Res.drawable.ic_arrow_up_with_tail),
                                        contentDescription = null,
                                        tint = AppTheme.colors.icon
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                if (indexInList != size - 1) {
                                    Icon(
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .size(36.dp)
                                            .rotate(180f)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() },
                                                onClick = { onDownClicked(state.id) }
                                            ),
                                        imageVector = vectorResource(Res.drawable.ic_arrow_up_with_tail),
                                        contentDescription = null,
                                        tint = AppTheme.colors.icon
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(widthDp = 360, heightDp = 800)
fun CommandView_Preview() {
    CommandsBoxTheme {
        Column(
            modifier = Modifier
                .background(AppTheme.colors.backgroundMain)
                .padding(16.dp)
        ) {
            val cascadiaFont = FontFamily(Font(Res.font.CascadiaCodeItalic))
            CommandView(
                state = CommandState(
                    id = 0,
                    isEditMode = false,
                    command = "sudo apt get",
                    description = "blablabla bla bla bla blabla bla"
                ),
                fontFamily = cascadiaFont,
                indexInList = 0,
                size = 2,
                onCommandQueryChange = { i, j -> },
                onDescriptionQueryChange = { i, j -> },
                onCloseEditClicked = {},
                onEditApplyClicked = {},
                onCopyClicked = {},
                onEnableEditClicked = {},
                onDeleteClicked = {},
                onUpClicked = {},
                onDownClicked = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
            CommandView(
                state = CommandState(
                    id = 1,
                    isEditMode = true
                ),
                fontFamily = cascadiaFont,
                indexInList = 1,
                size = 2,
                onCommandQueryChange = { i, j -> },
                onDescriptionQueryChange = { i, j -> },
                onCloseEditClicked = {},
                onEditApplyClicked = {},
                onCopyClicked = {},
                onEnableEditClicked = {},
                onDeleteClicked = {},
                onUpClicked = {},
                onDownClicked = {}
            )
        }
    }
}