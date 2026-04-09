package com.magictools.commandsBox.commonui.customview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.magictools.commandsBox.commonui.theme.AppTextTokens
import com.magictools.commandsBox.commonui.theme.AppTheme

@Composable
fun CommandsBoxButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .background(color = AppTheme.colors.buttonBackground, shape = RoundedCornerShape(8.dp))
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { onClick() }
        )
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text.uppercase(),
            style = AppTextTokens.bodyMedium.copy(color = AppTheme.colors.buttonText),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}