package com.magictools.commandsbox.data

import com.magictools.commandsbox.data.internal.MainMapper
import com.magictools.commandsbox.data.internal.MainRepositoryImpl
import com.magictools.commandsbox.data.internal.ThemePreferencesImpl
import com.magictools.commandsbox.data.internal.db.getRoomDatabase
import com.russhwolf.settings.Settings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
object DataBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideMainRepository(databaseBuilder: DatabaseBuilder): MainRepository {
        val database = getRoomDatabase(databaseBuilder.roomBuilder)
        return MainRepositoryImpl(database.companionsDao(), MainMapper())
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideSettings(): Settings = Settings()

    @Provides
    @SingleIn(AppScope::class)
    fun provideThemePreferences(settings: Settings): ThemePreferences =
        ThemePreferencesImpl(settings)
}
