package com.magictools.composeui.main.utils

import org.jetbrains.compose.resources.StringResource

class StringProviderImpl : StringProvider {

    override suspend fun getString(
        resId: StringResource,
        vararg formatArgs: Any
    ): String {
        return org.jetbrains.compose.resources.getString(resId, *formatArgs)
    }
}