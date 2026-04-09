package com.magictools.commandsBox.commonui.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.magictools.commandsBox.commonui.customview.AddBannerView
import com.magictools.commandsBox.commonui.customview.AddBannerViewState
import com.magictools.commandsBox.commonui.customview.CommandState
import com.magictools.commandsBox.commonui.customview.CommandView
import com.magictools.commandsBox.commonui.customview.InputViewState
import com.magictools.commandsBox.commonui.onDesktopScroll
import com.magictools.commandsBox.commonui.theme.AppTextTokens
import com.magictools.commandsBox.commonui.theme.AppTheme
import com.magictools.commandsBox.commonui.theme.CommandsBoxTheme
import com.magictools.commandsbox.resources.CascadiaCodeItalic
import com.magictools.commandsbox.resources.Res
import com.magictools.commandsbox.resources.add_command_hint
import com.magictools.commandsbox.resources.add_description_hint
import com.magictools.commandsbox.resources.add_tab_name_hint
import com.magictools.commandsbox.resources.app_name
import com.magictools.commandsbox.resources.commands_in_tab_empty
import com.magictools.commandsbox.resources.commandsbox_logo_transparent
import com.magictools.commandsbox.resources.ic_arrow_right
import com.magictools.commandsbox.resources.ic_delete
import com.magictools.commandsbox.resources.ic_edit
import com.magictools.multimvi.action.ViewAction
import com.magictools.multimvi.collectActions
import com.magictools.multimvi.event.ViewEvent
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()
    val state = viewModel.viewState.collectAsState()
    viewModel.action.collectActions<HomeAction>(
        dismiss = {

        },
        handle = {

        }
    )
    HomeView(state.value, viewModel.onNewEvent)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun HomeView(
    state: HomeViewState,
    onEvent: (HomeEvent) -> Unit
) {
    val cascadiaFont = FontFamily(Font(Res.font.CascadiaCodeItalic))
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.backgroundMain)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        color = AppTheme.colors.backgroundSecondary,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(AppTheme.colors.backgroundMain)
                            .padding(4.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.commandsbox_logo_transparent),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(Res.string.app_name),
                        fontFamily = cascadiaFont,
                        style = AppTextTokens.headlineLarge.copy(color = AppTheme.colors.mainText)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            when {
                state.tabs.isEmpty() -> {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                else -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState)
                            .onDesktopScroll(
                                onScroll = { delta ->
                                    coroutineScope.launch { scrollState.scrollBy(delta * 30f) }
                                }
                            )
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        state.tabs.forEachIndexed { index, tabItem ->
                            Column(
                                modifier = Modifier
                                    .width(IntrinsicSize.Max)
                                    .clickable { onEvent(OnTabSelected(tabItem.id)) }
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (index != 0) {
                                        Icon(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .rotate(180f)
                                                .clickable(
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    onClick = { onEvent(OnTabLeftClicked(tabItem.id)) }
                                                ),
                                            imageVector = vectorResource(Res.drawable.ic_arrow_right),
                                            contentDescription = null,
                                            tint = AppTheme.colors.icon
                                        )
                                    }
                                    Text(
                                        modifier = Modifier
                                            .padding(horizontal = 12.dp),
                                        text = tabItem.name,
                                        style = AppTextTokens.headlineMedium.copy(color = AppTheme.colors.mainText),
                                    )
                                    Icon(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() },
                                                onClick = { onEvent(OnTabEditClicked(tabItem.id)) }
                                            ),
                                        imageVector = vectorResource(Res.drawable.ic_edit),
                                        contentDescription = null,
                                        tint = AppTheme.colors.icon
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Icon(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() },
                                                onClick = { onEvent(OnDeleteTabClicked(tabItem.id)) }
                                            ),
                                        imageVector = vectorResource(Res.drawable.ic_delete),
                                        contentDescription = null,
                                        tint = AppTheme.colors.icon
                                    )
                                    if (index != state.tabs.size - 1) {
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Icon(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clickable(
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    onClick = { onEvent(OnTabRightClicked(tabItem.id)) }
                                                ),
                                            imageVector = vectorResource(Res.drawable.ic_arrow_right),
                                            contentDescription = null,
                                            tint = AppTheme.colors.icon
                                        )
                                    }
                                }
                                if (state.selectedTabId == tabItem.id) {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .height(4.dp)
                                            .padding(horizontal = 2.dp)
                                            .fillMaxWidth()
                                            .background(
                                                color = AppTheme.colors.icon,
                                                shape = CircleShape
                                            )
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier.padding(top = 4.dp).height(3.dp)
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    AddBannerView(
                        state = state.addBannerViewState,
                        onTabQueryChange = { onEvent(OnTabQueryChange(it)) },
                        onCommandQueryChange = { onEvent(OnCommandQueryChange(it)) },
                        onDescriptionQueryChange = { onEvent(OnDescriptionQueryChange(it)) },
                        onExpandIconClicked = { onEvent(OnAddBannerExpandIconClicked) },
                        onAddTabClicked = { onEvent(OnAddTabClicked) },
                        onAddCommandClicked = { onEvent(OnAddCommandClicked) }
                    )
                }
                when {
                    state.commands.isEmpty() -> {
                        item {
                            Text(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .padding(horizontal = 12.dp)
                                    .fillMaxWidth(),
                                text = stringResource(Res.string.commands_in_tab_empty),
                                style = AppTextTokens.headlineMedium.copy(color = AppTheme.colors.mainText),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    else -> {
                        itemsIndexed(state.commands) { index, commandState ->
                            CommandView(
                                state = commandState,
                                fontFamily = cascadiaFont,
                                indexInList = index,
                                size = state.commands.size,
                                onCommandQueryChange = { id, text ->
                                    onEvent(OnEditCommandQueryChange(id, text))
                                },
                                onDescriptionQueryChange = { id, text ->
                                    onEvent(OnEditDescriptionQueryChange(id, text))
                                },
                                onCloseEditClicked = { onEvent(OnEditCloseClicked(it)) },
                                onEditApplyClicked = { onEvent(OnEditApplyClicked(it)) },
                                onCopyClicked = { onEvent(OnCopyCommandClicked(it)) },
                                onEnableEditClicked = { onEvent(OnEnableEditClicked(it)) },
                                onDeleteClicked = { onEvent(OnDeleteCommandClicked(it)) },
                                onUpClicked = { onEvent(OnCommandUpClicked(it)) },
                                onDownClicked = { onEvent(OnCommandDownClicked(it)) }
                            )
                        }
                    }
                }
            }
        }
        if (state.deleteCommandDialogState.isVisible) {
            AlertDialog(
                onDismissRequest = { onEvent(OnDeleteCommandDialogDismiss) },
                title = { Text(text = state.deleteCommandDialogState.title) },
                text = {
                    Text(text = state.deleteCommandDialogState.subtitle)
                },
                confirmButton = {
                    TextButton(onClick = { onEvent(OnDeleteCommandDialogConfirm) }) {
                        Text(text = state.deleteCommandDialogState.confirmButton)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(OnDeleteCommandDialogDismiss) }) {
                        Text(text = state.deleteCommandDialogState.dismissButton)
                    }
                }
            )
        }
        if (state.editTabDialogState.isVisible) {
            AlertDialog(
                onDismissRequest = { onEvent(OnTabEditDialogDismiss) },
                title = { Text(text = state.editTabDialogState.title) },
                text = {
                    OutlinedTextField(
                        value = state.editTabDialogState.initialName,
                        onValueChange = { onEvent(OnTabEditDialogTextChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )
                },
                confirmButton = {
                    TextButton(onClick = { onEvent(OnTabEditDialogConfirm) }) {
                        Text(text = state.editTabDialogState.confirmButton)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(OnTabEditDialogDismiss) }) {
                        Text(text = state.editTabDialogState.dismissButton)
                    }
                }
            )
        }
        if (state.deleteTabDialogState.isVisible) {
            AlertDialog(
                onDismissRequest = { onEvent(OnDeleteTabDialogDismiss) },
                title = { Text(text = state.deleteTabDialogState.title) },
                text = {
                    Text(text = state.deleteTabDialogState.subtitle)
                },
                confirmButton = {
                    TextButton(onClick = { onEvent(OnDeleteTabDialogConfirm) }) {
                        Text(text = state.deleteTabDialogState.confirmButton)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(OnDeleteTabDialogDismiss) }) {
                        Text(text = state.deleteTabDialogState.dismissButton)
                    }
                }
            )
        }
    }
}

internal sealed interface HomeAction : ViewAction

internal sealed interface HomeEvent : ViewEvent
internal class OnTabSelected(val tabId: Long) : HomeEvent
internal data object OnAddBannerExpandIconClicked : HomeEvent
internal data object OnAddTabClicked : HomeEvent
internal data object OnAddCommandClicked : HomeEvent
internal class OnTabQueryChange(val text: String) : HomeEvent
internal class OnCommandQueryChange(val text: String) : HomeEvent
internal class OnDescriptionQueryChange(val text: String) : HomeEvent

internal class OnEditCommandQueryChange(val id: Long, val text: String) : HomeEvent
internal class OnEditDescriptionQueryChange(val id: Long, val text: String) : HomeEvent
internal class OnEditCloseClicked(val id: Long) : HomeEvent
internal class OnEditApplyClicked(val id: Long) : HomeEvent
internal class OnCopyCommandClicked(val id: Long) : HomeEvent
internal class OnCommandUpClicked(val id: Long) : HomeEvent
internal class OnCommandDownClicked(val id: Long) : HomeEvent
internal class OnEnableEditClicked(val id: Long) : HomeEvent
internal class OnDeleteCommandClicked(val id: Long) : HomeEvent
internal data object OnDeleteCommandDialogDismiss : HomeEvent
internal data object OnDeleteCommandDialogConfirm : HomeEvent
internal class OnTabLeftClicked(val id: Long) : HomeEvent
internal class OnTabRightClicked(val id: Long) : HomeEvent
internal class OnTabEditClicked(val id: Long) : HomeEvent
internal data object OnTabEditDialogDismiss : HomeEvent
internal data object OnTabEditDialogConfirm : HomeEvent
internal class OnTabEditDialogTextChanged(val text: String) : HomeEvent
internal class OnDeleteTabClicked(val id: Long) : HomeEvent
internal data object OnDeleteTabDialogDismiss : HomeEvent
internal data object OnDeleteTabDialogConfirm : HomeEvent

@Composable
@Preview(widthDp = 411, heightDp = 800)
@Preview(widthDp = 411, heightDp = 800, uiMode = UI_MODE_NIGHT_YES)
@Preview(widthDp = 360, heightDp = 800)
@Preview(widthDp = 600, heightDp = 800)
@Preview(widthDp = 840, heightDp = 800)
private fun SplashScreen_Preview() {
    CommandsBoxTheme {
        HomeView(
            state = HomeViewState(
                tabs = listOf(
                    HomeViewState.HomeTab(
                        0L,
                        "Docker",
                    ),
                    HomeViewState.HomeTab(
                        1L,
                        "Common",
                    ),
                    HomeViewState.HomeTab(
                        2L,
                        "Android",
                    ),
                    HomeViewState.HomeTab(
                        3L,
                        "System",
                    ),
                    HomeViewState.HomeTab(
                        4L,
                        "Other",
                    ),
                ),
                addBannerViewState = AddBannerViewState(
                    isExpanded = false,
                    hasTabs = true,
                    selectedTabName = "Docker",
                    tabInputViewState = InputViewState(
                        query = "",
                        hint = stringResource(Res.string.add_tab_name_hint),
                        maxLines = 1,
                        singleLine = true,
                        maxLength = 50,
                        icon = InputViewState.InputIcon.TAB
                    ),
                    commandInputViewState = InputViewState(
                        query = "",
                        hint = stringResource(Res.string.add_command_hint),
                        maxLines = 1,
                        singleLine = true,
                        maxLength = 50,
                        icon = InputViewState.InputIcon.COMMAND
                    ),
                    descriptionInputViewState = InputViewState(
                        query = "",
                        hint = stringResource(Res.string.add_description_hint),
                        maxLines = 3,
                        singleLine = false,
                        maxLength = 50,
                        icon = InputViewState.InputIcon.DESCRIPTION
                    )
                ),
                commands = buildList {
                    add(
                        CommandState(
                            id = 0,
                            isEditMode = true,
                            commandInputViewState = InputViewState(
                                icon = InputViewState.InputIcon.COMMAND
                            ),
                            descriptionInputViewState = InputViewState(
                                icon = InputViewState.InputIcon.DESCRIPTION
                            ),
                        )
                    )
                    add(
                        CommandState(
                            id = 1,
                            isEditMode = false,
                            command = "sudo apt get update",
                            description = "Update system",
                        )
                    )
                }
            ),
            {}
        )
    }
}