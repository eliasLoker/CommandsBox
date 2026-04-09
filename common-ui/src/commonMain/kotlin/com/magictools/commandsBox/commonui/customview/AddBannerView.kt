package com.magictools.commandsBox.commonui.customview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.magictools.commandsBox.commonui.theme.AppTextTokens
import com.magictools.commandsBox.commonui.theme.AppTheme
import com.magictools.commandsBox.commonui.theme.CommandsBoxTheme
import com.magictools.commandsbox.resources.Res
import com.magictools.commandsbox.resources.add_command_button
import com.magictools.commandsbox.resources.add_command_to_tab_title
import com.magictools.commandsbox.resources.add_tab
import com.magictools.commandsbox.resources.add_tab_button
import com.magictools.commandsbox.resources.add_tab_or_command_title
import com.magictools.commandsbox.resources.ic_add_square
import com.magictools.commandsbox.resources.ic_arrow_up
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Serializable
data class AddBannerViewState(
    val isExpanded: Boolean = false,
    val tabInputViewState: InputViewState = InputViewState(),
    val commandInputViewState: InputViewState = InputViewState(),
    val descriptionInputViewState: InputViewState = InputViewState(),
    val hasTabs: Boolean = false,
    val selectedTabName: String = ""
)

@Composable
fun AddBannerView(
    state: AddBannerViewState,
    onTabQueryChange: (String) -> Unit,
    onCommandQueryChange: (String) -> Unit,
    onDescriptionQueryChange: (String) -> Unit,
    onExpandIconClicked: () -> Unit,
    onAddTabClicked: () -> Unit,
    onAddCommandClicked: () -> Unit
) {
    val arrowRotation by animateFloatAsState(
        targetValue = if (state.isExpanded) 180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(
                color = AppTheme.colors.backgroundSecondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onExpandIconClicked() }
                    ),
                imageVector = vectorResource(Res.drawable.ic_add_square),
                contentDescription = null,
                tint = AppTheme.colors.icon
            )
            Text(
                modifier = Modifier.padding(start = 16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onExpandIconClicked() }
                    ),
                text = stringResource(
                    when {
                        state.hasTabs -> Res.string.add_tab_or_command_title
                        else -> Res.string.add_tab
                    }
                ),
                style = AppTextTokens.bodyLarge.copy(color = AppTheme.colors.mainText)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .rotate(arrowRotation)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onExpandIconClicked() }
                    ),
                imageVector = vectorResource(Res.drawable.ic_arrow_up),
                contentDescription = null,
                tint = AppTheme.colors.icon
            )
        }
        AnimatedVisibility(
            visible = state.isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                InputView(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    state = state.tabInputViewState,
                    onQueryChange = onTabQueryChange
                )
                CommandsBoxButton(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(Res.string.add_tab_button),
                    onClick = { onAddTabClicked() }
                )
                if (state.hasTabs) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth()
                            .background(AppTheme.colors.backgroundMain)
                            .height(1.dp)
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(
                            Res.string.add_command_to_tab_title,
                            state.selectedTabName
                        ),
                        style = AppTextTokens.bodyMedium.copy(AppTheme.colors.secondaryText)
                    )
                    InputView(
                        modifier = Modifier
                            .padding(top = 16.dp),
                        state = state.commandInputViewState,
                        onQueryChange = onCommandQueryChange
                    )
                    InputView(
                        modifier = Modifier
                            .padding(top = 8.dp),
                        state = state.descriptionInputViewState,
                        onQueryChange = onDescriptionQueryChange
                    )
                    CommandsBoxButton(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(Res.string.add_command_button),
                        onClick = { onAddCommandClicked() }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(widthDp = 360, heightDp = 800)
private fun AddBannerView_Preview() {
    CommandsBoxTheme {
        Column(
            modifier = Modifier.background(color = AppTheme.colors.backgroundMain)
                .padding(16.dp)
        ) {
            AddBannerView(
                state = AddBannerViewState(),
                onTabQueryChange = {},
                onCommandQueryChange = {},
                onDescriptionQueryChange = {},
                onExpandIconClicked = {},
                onAddTabClicked = {},
                onAddCommandClicked = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
            AddBannerView(
                state = AddBannerViewState(
                    isExpanded = true
                ),
                onTabQueryChange = {},
                onCommandQueryChange = {},
                onDescriptionQueryChange = {},
                onExpandIconClicked = {},
                onAddTabClicked = {},
                onAddCommandClicked = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
            AddBannerView(
                state = AddBannerViewState(
                    isExpanded = true,
                    hasTabs = true,
                    selectedTabName = "Docker"
                ),
                onTabQueryChange = {},
                onCommandQueryChange = {},
                onDescriptionQueryChange = {},
                onExpandIconClicked = {},
                onAddTabClicked = {},
                onAddCommandClicked = {}
            )
        }
    }
}