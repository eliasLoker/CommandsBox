package com.magictools.commandsBox.commonui.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.magictools.commandsBox.commonui.customview.AddBannerView
import com.magictools.commandsBox.commonui.customview.AddBannerViewState
import com.magictools.commandsBox.commonui.customview.CommandState
import com.magictools.commandsBox.commonui.customview.CommandView
import com.magictools.commandsBox.commonui.customview.InputViewState
import com.magictools.commandsBox.commonui.onDesktopScroll
import com.magictools.commandsBox.commonui.theme.AppShapes
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
import com.magictools.commandsbox.resources.ic_add_square
import com.magictools.commandsbox.resources.ic_arrow_right
import com.magictools.commandsbox.resources.ic_delete
import com.magictools.commandsbox.resources.ic_edit
import com.magictools.commandsbox.resources.ic_settings
import com.magictools.commandsbox.resources.settings_title
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
internal fun HomeScreen(
    onSettingsClicked: () -> Unit
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state = viewModel.viewState.collectAsState()
    viewModel.action.collectActions<HomeAction>(
        dismiss = {

        },
        handle = {

        }
    )
    HomeView(
        state = state.value,
        onSettingsClicked = onSettingsClicked,
        onEvent = viewModel.onNewEvent
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun HomeView(
    state: HomeViewState,
    onSettingsClicked: () -> Unit,
    onEvent: (HomeEvent) -> Unit
) {
    val cascadiaFont = FontFamily(Font(Res.font.CascadiaCodeItalic))
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val contentSnapshot = remember(state.selectedTabId, state.commands) {
        HomeContentSnapshot(tabId = state.selectedTabId, commands = state.commands)
    }
    val tabSlideDistancePx = with(LocalDensity.current) { 64.dp.roundToPx() }

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
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                AppTheme.colors.accent.copy(alpha = 0.10f),
                                AppTheme.colors.backgroundSecondary
                            )
                        ),
                        shape = AppShapes.banner
                    )
                    .padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.CenterStart),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .shadow(elevation = 4.dp, shape = CircleShape)
                                .clip(CircleShape)
                                .background(AppTheme.colors.backgroundMain)
                                .border(
                                    width = 1.5.dp,
                                    color = AppTheme.colors.accent,
                                    shape = CircleShape
                                )
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
                            style = AppTextTokens.headlineLarge.copy(
                                color = AppTheme.colors.mainText,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                    TabActionIcon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        imageVector = vectorResource(Res.drawable.ic_settings),
                        tint = AppTheme.colors.accentText,
                        rotateDegrees = 0f,
                        contentDescription = stringResource(Res.string.settings_title),
                        onClick = onSettingsClicked
                    )
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
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.tabs.forEachIndexed { index, tabItem ->
                            val isSelected = state.selectedTabId == tabItem.id
                            val pillColor by animateColorAsState(
                                targetValue = if (isSelected) {
                                    AppTheme.colors.accent.copy(alpha = 0.15f)
                                } else {
                                    Color.Transparent
                                },
                                label = "tabPillColor"
                            )
                            Column(
                                modifier = Modifier
                                    .clip(AppShapes.medium)
                                    .background(color = pillColor, shape = AppShapes.medium)
                                    .clickable { onEvent(OnTabSelected(tabItem.id)) }
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.heightIn(min = 36.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AnimatedVisibility(
                                        visible = isSelected && index != 0,
                                        enter = fadeIn(
                                            animationSpec = tween(
                                                durationMillis = 200,
                                                delayMillis = 60
                                            )
                                        ) + expandHorizontally(
                                            animationSpec = tween(
                                                durationMillis = 280,
                                                delayMillis = 40,
                                                easing = FastOutSlowInEasing
                                            ),
                                            expandFrom = Alignment.End
                                        ),
                                        exit = fadeOut(
                                            animationSpec = tween(durationMillis = 120)
                                        ) + shrinkHorizontally(
                                            animationSpec = tween(
                                                durationMillis = 200,
                                                easing = FastOutSlowInEasing
                                            ),
                                            shrinkTowards = Alignment.End
                                        )
                                    ) {
                                        TabActionIcon(
                                            imageVector = vectorResource(Res.drawable.ic_arrow_right),
                                            tint = AppTheme.colors.accentText,
                                            rotateDegrees = 180f,
                                            onClick = { onEvent(OnTabLeftClicked(tabItem.id)) }
                                        )
                                    }
                                    Text(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp),
                                        text = tabItem.name,
                                        style = AppTextTokens.headlineMedium.copy(
                                            color = if (isSelected) {
                                                AppTheme.colors.accentText
                                            } else {
                                                AppTheme.colors.secondaryText
                                            }
                                        ),
                                    )
                                    AnimatedVisibility(
                                        visible = isSelected,
                                        enter = fadeIn(
                                            animationSpec = tween(
                                                durationMillis = 200,
                                                delayMillis = 60
                                            )
                                        ) + expandHorizontally(
                                            animationSpec = tween(
                                                durationMillis = 280,
                                                delayMillis = 40,
                                                easing = FastOutSlowInEasing
                                            ),
                                            expandFrom = Alignment.Start
                                        ),
                                        exit = fadeOut(
                                            animationSpec = tween(durationMillis = 120)
                                        ) + shrinkHorizontally(
                                            animationSpec = tween(
                                                durationMillis = 200,
                                                easing = FastOutSlowInEasing
                                            ),
                                            shrinkTowards = Alignment.Start
                                        )
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            TabActionIcon(
                                                imageVector = vectorResource(Res.drawable.ic_edit),
                                                tint = AppTheme.colors.accentText,
                                                rotateDegrees = 0f,
                                                onClick = { onEvent(OnTabEditClicked(tabItem.id)) }
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            TabActionIcon(
                                                imageVector = vectorResource(Res.drawable.ic_delete),
                                                tint = AppTheme.colors.accentText,
                                                rotateDegrees = 0f,
                                                onClick = { onEvent(OnDeleteTabClicked(tabItem.id)) }
                                            )
                                            if (index != state.tabs.size - 1) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                TabActionIcon(
                                                    imageVector = vectorResource(Res.drawable.ic_arrow_right),
                                                    tint = AppTheme.colors.accentText,
                                                    rotateDegrees = 0f,
                                                    onClick = { onEvent(OnTabRightClicked(tabItem.id)) }
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
            AddBannerView(
                state = state.addBannerViewState,
                onTabQueryChange = { onEvent(OnTabQueryChange(it)) },
                onCommandQueryChange = { onEvent(OnCommandQueryChange(it)) },
                onDescriptionQueryChange = { onEvent(OnDescriptionQueryChange(it)) },
                onExpandIconClicked = { onEvent(OnAddBannerExpandIconClicked) },
                onAddTabClicked = { onEvent(OnAddTabClicked) },
                onAddCommandClicked = { onEvent(OnAddCommandClicked) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedContent(
                targetState = contentSnapshot,
                contentKey = { it.tabId },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clipToBounds(),
                transitionSpec = {
                    val fromIndex = state.tabs.indexOfFirst { it.id == initialState.tabId }
                    val toIndex = state.tabs.indexOfFirst { it.id == targetState.tabId }
                    when {
                        fromIndex == -1 || toIndex == -1 -> {
                            EnterTransition.None togetherWith ExitTransition.None
                        }

                        else -> {
                            val direction = if (toIndex >= fromIndex) 1 else -1
                            (
                                slideInHorizontally(
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    ),
                                    initialOffsetX = { direction * tabSlideDistancePx }
                                ) + fadeIn(animationSpec = tween(durationMillis = 220))
                            ) togetherWith (
                                slideOutHorizontally(
                                    animationSpec = tween(
                                        durationMillis = 240,
                                        easing = FastOutSlowInEasing
                                    ),
                                    targetOffsetX = { -direction * tabSlideDistancePx }
                                ) + fadeOut(animationSpec = tween(durationMillis = 160))
                            ) using SizeTransform(clip = true)
                        }
                    }
                },
                label = "homeContent"
            ) { content ->
                AnimatedContent(
                    targetState = content.commands,
                    contentKey = { it.isEmpty() },
                    modifier = Modifier.fillMaxSize(),
                    label = "homeEmptyState"
                ) { commands ->
                    when {
                        commands.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        modifier = Modifier.size(56.dp),
                                        imageVector = vectorResource(Res.drawable.ic_add_square),
                                        contentDescription = null,
                                        tint = AppTheme.colors.accent
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = stringResource(Res.string.commands_in_tab_empty),
                                        style = AppTextTokens.bodyLarge.copy(color = AppTheme.colors.secondaryText),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                itemsIndexed(commands) { index, commandState ->
                                    CommandView(
                                        modifier = Modifier.animateItem(),
                                        state = commandState,
                                        fontFamily = cascadiaFont,
                                        indexInList = index,
                                        size = commands.size,
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
            }
        }
        if (state.deleteCommandDialogState.isVisible) {
            AlertDialog(
                onDismissRequest = { onEvent(OnDeleteCommandDialogDismiss) },
                shape = AppShapes.large,
                title = {
                    Text(
                        text = state.deleteCommandDialogState.title,
                        style = AppTextTokens.headlineSmall.copy(color = AppTheme.colors.mainText)
                    )
                },
                text = {
                    Text(
                        text = state.deleteCommandDialogState.subtitle,
                        style = AppTextTokens.bodyMedium.copy(color = AppTheme.colors.secondaryText)
                    )
                },
                confirmButton = {
                    TextButton(onClick = { onEvent(OnDeleteCommandDialogConfirm) }) {
                        Text(
                            text = state.deleteCommandDialogState.confirmButton,
                            style = AppTextTokens.headlineSmall.copy(color = AppTheme.colors.destructive)
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(OnDeleteCommandDialogDismiss) }) {
                        Text(
                            text = state.deleteCommandDialogState.dismissButton,
                            style = AppTextTokens.headlineSmall.copy(color = AppTheme.colors.secondaryText)
                        )
                    }
                }
            )
        }
        if (state.editTabDialogState.isVisible) {
            AlertDialog(
                onDismissRequest = { onEvent(OnTabEditDialogDismiss) },
                shape = AppShapes.large,
                title = {
                    Text(
                        text = state.editTabDialogState.title,
                        style = AppTextTokens.headlineSmall.copy(color = AppTheme.colors.mainText)
                    )
                },
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
                        Text(
                            text = state.editTabDialogState.confirmButton,
                            style = AppTextTokens.headlineSmall.copy(color = AppTheme.colors.accentText)
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(OnTabEditDialogDismiss) }) {
                        Text(
                            text = state.editTabDialogState.dismissButton,
                            style = AppTextTokens.headlineSmall.copy(color = AppTheme.colors.secondaryText)
                        )
                    }
                }
            )
        }
        if (state.deleteTabDialogState.isVisible) {
            AlertDialog(
                onDismissRequest = { onEvent(OnDeleteTabDialogDismiss) },
                shape = AppShapes.large,
                title = {
                    Text(
                        text = state.deleteTabDialogState.title,
                        style = AppTextTokens.headlineSmall.copy(color = AppTheme.colors.mainText)
                    )
                },
                text = {
                    Text(
                        text = state.deleteTabDialogState.subtitle,
                        style = AppTextTokens.bodyMedium.copy(color = AppTheme.colors.secondaryText)
                    )
                },
                confirmButton = {
                    TextButton(onClick = { onEvent(OnDeleteTabDialogConfirm) }) {
                        Text(
                            text = state.deleteTabDialogState.confirmButton,
                            style = AppTextTokens.headlineSmall.copy(color = AppTheme.colors.destructive)
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(OnDeleteTabDialogDismiss) }) {
                        Text(
                            text = state.deleteTabDialogState.dismissButton,
                            style = AppTextTokens.headlineSmall.copy(color = AppTheme.colors.secondaryText)
                        )
                    }
                }
            )
        }
    }
}

private data class HomeContentSnapshot(
    val tabId: Long,
    val commands: List<CommandState>
)

@Composable
private fun TabActionIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color,
    rotateDegrees: Float,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(color = AppTheme.colors.backgroundMain, shape = CircleShape)
            .clickable(onClick = onClick)
            .rotate(rotateDegrees)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

sealed interface HomeAction : ViewAction

sealed interface HomeEvent : ViewEvent
class OnTabSelected(val tabId: Long) : HomeEvent
data object OnAddBannerExpandIconClicked : HomeEvent
data object OnAddTabClicked : HomeEvent
data object OnAddCommandClicked : HomeEvent
class OnTabQueryChange(val text: String) : HomeEvent
class OnCommandQueryChange(val text: String) : HomeEvent
class OnDescriptionQueryChange(val text: String) : HomeEvent

class OnEditCommandQueryChange(val id: Long, val text: String) : HomeEvent
class OnEditDescriptionQueryChange(val id: Long, val text: String) : HomeEvent
class OnEditCloseClicked(val id: Long) : HomeEvent
class OnEditApplyClicked(val id: Long) : HomeEvent
class OnCopyCommandClicked(val id: Long) : HomeEvent
class OnCommandUpClicked(val id: Long) : HomeEvent
class OnCommandDownClicked(val id: Long) : HomeEvent
class OnEnableEditClicked(val id: Long) : HomeEvent
class OnDeleteCommandClicked(val id: Long) : HomeEvent
data object OnDeleteCommandDialogDismiss : HomeEvent
data object OnDeleteCommandDialogConfirm : HomeEvent
class OnTabLeftClicked(val id: Long) : HomeEvent
class OnTabRightClicked(val id: Long) : HomeEvent
class OnTabEditClicked(val id: Long) : HomeEvent
data object OnTabEditDialogDismiss : HomeEvent
data object OnTabEditDialogConfirm : HomeEvent
class OnTabEditDialogTextChanged(val text: String) : HomeEvent
class OnDeleteTabClicked(val id: Long) : HomeEvent
data object OnDeleteTabDialogDismiss : HomeEvent
data object OnDeleteTabDialogConfirm : HomeEvent

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
            onSettingsClicked = {},
            onEvent = {}
        )
    }
}