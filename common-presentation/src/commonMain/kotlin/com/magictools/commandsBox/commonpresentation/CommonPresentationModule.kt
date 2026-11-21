package com.magictools.commandsBox.commonpresentation

import com.magictools.commandsBox.commonpresentation.ui.home.HomeViewModel
import com.magictools.commandsBox.commonpresentation.ui.main.MainToastEvents
import com.magictools.commandsBox.commonpresentation.ui.main.MainViewModel
import com.magictools.commandsBox.commonpresentation.ui.settings.SettingsViewModel
import com.magictools.commandsBox.commonpresentation.utils.stringprovider.StringProvider
import com.magictools.commandsBox.commonpresentation.utils.stringprovider.StringProviderImpl
import com.magictools.commandsbox.data.dataModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonPresentationModule = module {
    includes(dataModule)
    singleOf(::MainToastEvents)
    viewModelOf(::HomeViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)

    factoryOf<StringProvider>(::StringProviderImpl)
}
