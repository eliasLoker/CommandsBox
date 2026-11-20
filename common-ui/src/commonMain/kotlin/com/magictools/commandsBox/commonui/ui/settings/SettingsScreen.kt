package com.magictools.commandsBox.commonui.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.magictools.commandsBox.commonui.theme.AppShapes
import com.magictools.commandsBox.commonui.theme.AppTextTokens
import com.magictools.commandsBox.commonui.theme.AppTheme
import com.magictools.commandsBox.commonui.theme.CommandsBoxTheme
import com.magictools.commandsBox.commonui.theme.resolveDarkTheme
import com.magictools.commandsbox.resources.Res
import com.magictools.commandsbox.resources.ic_arrow_right
import com.magictools.commandsbox.resources.settings_back
import com.magictools.commandsbox.resources.settings_dark_theme
import com.magictools.commandsbox.resources.settings_theme_current
import com.magictools.commandsbox.resources.settings_theme_dark
import com.magictools.commandsbox.resources.settings_theme_light
import com.magictools.commandsbox.resources.settings_title
import com.magictools.multimvi.action.ViewAction
import com.magictools.multimvi.collectActions
import com.magictools.multimvi.event.ViewEvent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Suppress("UNUSED_PARAMETER")
@Composable
internal fun SettingsScreen(
    argument: SettingsArgument,
    onBack: () -> Unit
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val state = viewModel.viewState.collectAsState()
    viewModel.action.collectActions<SettingsAction>(
        dismiss = {

        },
        handle = { action ->
            when (action) {
                SettingsAction.NavigateBack -> onBack()
            }
        }
    )
    SettingsView(state.value, viewModel.onNewEvent)
}

@Composable
private fun SettingsView(
    state: SettingsViewState,
    onEvent: (SettingsEvent) -> Unit
) {
    val isDarkTheme = resolveDarkTheme(
        selectedTheme = state.selectedTheme,
        systemInDarkTheme = isSystemInDarkTheme()
    )
    val currentTheme = stringResource(
        if (isDarkTheme) {
            Res.string.settings_theme_dark
        } else {
            Res.string.settings_theme_light
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.backgroundMain)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingsIconButton(
                    imageVector = vectorResource(Res.drawable.ic_arrow_right),
                    contentDescription = stringResource(Res.string.settings_back),
                    rotateDegrees = 180f,
                    onClick = { onEvent(OnBackClicked) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(Res.string.settings_title),
                    style = AppTextTokens.headlineLarge.copy(color = AppTheme.colors.mainText)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AppShapes.large)
                    .background(color = AppTheme.colors.backgroundSecondary)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.settings_dark_theme),
                            style = AppTextTokens.headlineMedium.copy(color = AppTheme.colors.mainText)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(Res.string.settings_theme_current, currentTheme),
                            style = AppTextTokens.bodyMedium.copy(color = AppTheme.colors.secondaryText)
                        )
                    }
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onEvent(OnThemeChanged(it)) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AppTheme.colors.onAccent,
                            checkedTrackColor = AppTheme.colors.accent,
                            uncheckedThumbColor = AppTheme.colors.secondaryText,
                            uncheckedTrackColor = AppTheme.colors.backgroundMain,
                            uncheckedBorderColor = AppTheme.colors.secondaryText
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    rotateDegrees: Float,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(color = AppTheme.colors.backgroundSecondary, shape = CircleShape)
            .clickable(onClick = onClick)
            .rotate(rotateDegrees)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = AppTheme.colors.accentText
        )
    }
}

internal sealed interface SettingsAction : ViewAction {
    data object NavigateBack : SettingsAction
}

internal sealed interface SettingsEvent : ViewEvent
internal data object OnBackClicked : SettingsEvent
internal class OnThemeChanged(val isDark: Boolean) : SettingsEvent

@Composable
@Preview(widthDp = 411, heightDp = 800)
@Preview(widthDp = 411, heightDp = 800, uiMode = UI_MODE_NIGHT_YES)
@Preview(widthDp = 360, heightDp = 800)
@Preview(widthDp = 600, heightDp = 800)
@Preview(widthDp = 840, heightDp = 800)
private fun SettingsScreen_Preview() {
    CommandsBoxTheme {
        SettingsView(
            state = SettingsViewState(),
            onEvent = {}
        )
    }
}
