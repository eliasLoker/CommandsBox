package com.magictools.composeui.main.utils

import org.jetbrains.compose.resources.StringResource

interface StringProvider {

    suspend fun getString(resId: StringResource, vararg formatArgs: Any): String
}