package com.magictools.commandsBox.commonui

import com.magictools.commandsBox.commonui.utils.CustomClipboardManager
import com.magictools.commandsbox.data.desktopDataModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val desktopCommonUiModule = module {
    includes(desktopDataModule)
    singleOf(::CustomClipboardManagerImpl).binds(arrayOf(CustomClipboardManager::class))
}