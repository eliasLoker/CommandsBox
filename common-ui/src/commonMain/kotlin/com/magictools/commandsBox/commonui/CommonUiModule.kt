package com.magictools.commandsBox.commonui

import com.magictools.commandsBox.commonpresentation.commonPresentationModule
import org.koin.dsl.module

val commonUiModule = module {
    includes(commonPresentationModule)
}
