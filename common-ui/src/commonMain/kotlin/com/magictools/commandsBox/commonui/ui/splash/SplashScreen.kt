package com.magictools.commandsBox.commonui.ui.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.magictools.commandsBox.commonui.theme.AppTextTokens
import com.magictools.commandsBox.commonui.theme.AppTheme
import com.magictools.commandsBox.commonui.theme.CommandsBoxTheme
import com.magictools.commandsbox.resources.CascadiaCodeItalic
import com.magictools.commandsbox.resources.Res
import com.magictools.commandsbox.resources.app_name_splash
import com.magictools.commandsbox.resources.commandsbox_logo_transparent
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SplashScreen(
    onTimeOut: () -> Unit
) {
    val cascadiaFont = FontFamily(Font(Res.font.CascadiaCodeItalic))
    val title = stringResource(Res.string.app_name_splash)

    var logoAlpha by remember { mutableFloatStateOf(0f) }
    val visibleChars = remember { mutableStateOf(0) }
    val animatedLogoAlpha by animateFloatAsState(
        targetValue = logoAlpha,
        animationSpec = tween(durationMillis = 1500)
    )

    LaunchedEffect(Unit) {
        logoAlpha = 1f
        delay(1000)
        title.forEachIndexed { index, _ ->
            visibleChars.value = index + 1
            delay(100)
        }
        delay(500)
        onTimeOut()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundMain),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .size(180.dp)
                .graphicsLayer(alpha = animatedLogoAlpha)
                .clip(CircleShape)
                .background(AppTheme.colors.backgroundSecondary)
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.commandsbox_logo_transparent),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            text = title.take(visibleChars.value),
            textAlign = TextAlign.Center,
            fontFamily = cascadiaFont,
            style = AppTextTokens.headlineLarge.copy(
                color = AppTheme.colors.secondaryText,
                fontSize = 24.sp,
            )
        )
        Spacer(modifier = Modifier.weight(1.2f))
    }
}

@Composable
@Preview(widthDp = 411, heightDp = 800)
@Preview(widthDp = 411, heightDp = 800, uiMode = UI_MODE_NIGHT_YES)
@Preview(widthDp = 360, heightDp = 800)
@Preview(widthDp = 600, heightDp = 800)
@Preview(widthDp = 840, heightDp = 800)
private fun SplashScreen_Preview() {
    CommandsBoxTheme {
        SplashScreen {

        }
    }
}