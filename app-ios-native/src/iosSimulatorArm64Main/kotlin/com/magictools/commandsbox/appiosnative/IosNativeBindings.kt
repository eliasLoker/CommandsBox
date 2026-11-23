package com.magictools.commandsbox.appiosnative

import com.magictools.commandsBox.commonpresentation.utils.CustomClipboardManager
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
object IosNativeBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideCustomClipboardManager(): CustomClipboardManager =
        IosNativeClipboardManager()
}
