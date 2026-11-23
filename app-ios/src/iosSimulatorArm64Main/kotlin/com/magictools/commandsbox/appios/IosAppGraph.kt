package com.magictools.commandsbox.appios

import dev.zacsweers.metro.AppScope
import com.magictools.commandsbox.data.ThemePreferences
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(AppScope::class)
interface IosAppGraph : ViewModelGraph {

    val themePreferences: ThemePreferences
}
