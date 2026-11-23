package com.magictools.commandsbox.appandroid

import android.content.Context
import dev.zacsweers.metro.AppScope
import com.magictools.commandsbox.data.ThemePreferences
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(AppScope::class)
interface AppGraph : ViewModelGraph {

    val themePreferences: ThemePreferences

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides context: Context): AppGraph
    }
}
