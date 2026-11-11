package com.magictools.commandsBox.commonui

import com.magictools.commandsBox.commonui.utils.CustomClipboardManager
import com.magictools.commandsbox.data.iosDataModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val iosCommonUiModule = module {
    includes(iosDataModule)
    singleOf(::CustomClipboardManagerImpl).binds(arrayOf(CustomClipboardManager::class))
}
