package com.magictools.commandsBox.commonui

import com.magictools.commandsBox.commonui.utils.CustomClipboardManager
import com.magictools.commandsbox.data.androidDataModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val androidCommonUiModule = module {
    includes(androidDataModule)
    singleOf(::CustomClipboardManagerImpl).binds(arrayOf(CustomClipboardManager::class))
}