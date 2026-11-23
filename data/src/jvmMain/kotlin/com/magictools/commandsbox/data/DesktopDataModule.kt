package com.magictools.commandsbox.data

import androidx.room.Room
import com.magictools.commandsbox.data.internal.db.AppDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import java.io.File

@BindingContainer
@ContributesTo(AppScope::class)
object DesktopDataBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideDatabaseBuilder(): DatabaseBuilder {
        val homePath = System.getProperty("user.home")
        val dbFile = File(homePath, ".commandsbox/commandsbox.db")
        if (!dbFile.parentFile.exists()) {
            dbFile.parentFile.mkdirs()
        }
        return DatabaseBuilder(
            Room.databaseBuilder<AppDatabase>(
                name = dbFile.absolutePath
            )
        )
    }
}
