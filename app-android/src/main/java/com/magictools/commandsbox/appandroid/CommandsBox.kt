package com.magictools.commandsbox.appandroid

import android.app.Application
import dev.zacsweers.metro.createGraphFactory

class CommandsBox : Application() {

    lateinit var appGraph: AppGraph
        private set

    override fun onCreate() {
        super.onCreate()
        appGraph = createGraphFactory<AppGraph.Factory>().create(this)
    }
}
