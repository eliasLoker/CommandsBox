package com.magictools.commandsBox.commonpresentation.utils.stringprovider

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import org.jetbrains.compose.resources.StringResource

@ContributesBinding(AppScope::class)
class StringProviderImpl : StringProvider {

    override suspend fun getString(
        resId: StringResource,
        vararg formatArgs: Any
    ): String {
        return org.jetbrains.compose.resources.getString(resId, *formatArgs)
    }
}
