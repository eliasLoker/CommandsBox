package com.magictools.commandsBox.commonpresentation.utils.stringprovider

import org.jetbrains.compose.resources.StringResource

interface StringProvider {

    suspend fun getString(resId: StringResource, vararg formatArgs: Any): String
}
