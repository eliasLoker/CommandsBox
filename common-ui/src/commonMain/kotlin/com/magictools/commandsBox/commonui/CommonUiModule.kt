package com.magictools.commandsBox.commonui

import com.magictools.commandsBox.commonui.ui.home.HomeViewModel
import com.magictools.commandsBox.commonui.ui.main.MainToastEvents
import com.magictools.commandsBox.commonui.ui.main.MainViewModel
import com.magictools.commandsbox.data.dataModule
import com.magictools.composeui.main.utils.StringProvider
import com.magictools.composeui.main.utils.StringProviderImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonUiModule = module {
    includes(dataModule)
    singleOf(::MainToastEvents)
    viewModelOf(::HomeViewModel)
    viewModelOf(::MainViewModel)

    factoryOf<StringProvider>(::StringProviderImpl)
}