package com.magictools.commandsbox.appandroid

import android.app.Application
import com.magictools.commandsBox.commonui.androidCommonUiModule
import com.magictools.commandsBox.commonui.commonUiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class CommandsBox : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CommandsBox)
            modules(
                androidCommonUiModule,

                commonUiModule,
            )
        }
    }
}