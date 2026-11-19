package com.magictools.commandsBox.commonui.customview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.magictools.commandsBox.commonui.theme.AppShapes
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
    modifier: Modifier = Modifier,
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
        modifier = modifier.fillMaxWidth()
            .clip(AppShapes.large)
            .background(
                color = AppTheme.colors.backgroundSecondary,
                shape = AppShapes.large
            )
            .border(
                width = 1.dp,
                color = AppTheme.colors.icon.copy(alpha = 0.12f),
                shape = AppShapes.large
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        when {
            state.isEditMode -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.edit),
                        style = AppTextTokens.headlineSmall.copy(color = AppTheme.colors.secondaryText)
                    )
                    Icon(
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onCloseEditClicked(state.id) },
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
                            .clickable { onCopyClicked(state.id) },
                        imageVector = vectorResource(Res.drawable.ic_copy),
                        contentDescription = null,
                        tint = AppTheme.colors.accent
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
                                .padding(top = 16.dp)
                                .fillMaxWidth()
                                .clip(AppShapes.small)
                                .background(
                                    color = AppTheme.colors.backgroundMain,
                                    shape = AppShapes.small
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onEnableEditClicked(state.id) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(Res.string.edit).uppercase(),
                                    style = AppTextTokens.bodyMedium.copy(
                                        color = AppTheme.colors.accentText,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 3.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(16.dp)
                                    .background(color = AppTheme.colors.icon.copy(alpha = 0.2f))
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onDeleteClicked(state.id) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(Res.string.delete).uppercase(),
                                    style = AppTextTokens.bodyMedium.copy(
                                        color = AppTheme.colors.destructive,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 3.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        val isUpEnabled = indexInList != 0
                        val isDownEnabled = indexInList != size - 1
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    color = AppTheme.colors.backgroundMain,
                                    shape = CircleShape
                                )
                                .alpha(if (isUpEnabled) 1f else 0.3f)
                                .clickable(
                                    enabled = isUpEnabled,
                                    onClick = { onUpClicked(state.id) }
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = vectorResource(Res.drawable.ic_arrow_up_with_tail),
                                contentDescription = null,
                                tint = AppTheme.colors.icon
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    color = AppTheme.colors.backgroundMain,
                                    shape = CircleShape
                                )
                                .alpha(if (isDownEnabled) 1f else 0.3f)
                                .clickable(
                                    enabled = isDownEnabled,
                                    onClick = { onDownClicked(state.id) }
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .rotate(180f),
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