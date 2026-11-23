package com.magictools.commandsbox.appdesktop

import dev.zacsweers.metro.AppScope
import com.magictools.commandsbox.data.ThemePreferences
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(AppScope::class)
interface DesktopAppGraph : ViewModelGraph {

    val themePreferences: ThemePreferences
}
